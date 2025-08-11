package com.lcwd.gateway.config;

import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResilienceGatewayFilterFactory extends AbstractGatewayFilterFactory<ResilienceGatewayFilterFactory.Config> {

    @Autowired
    private io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private io.github.resilience4j.retry.RetryRegistry retryRegistry;

    @Autowired
    private io.github.resilience4j.ratelimiter.RateLimiterRegistry rateLimiterRegistry;

    public ResilienceGatewayFilterFactory() {
        super(Config.class);  // ✅ this only works if you're extending AbstractGatewayFilterFactory<Config>
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(config.getCircuitBreakerName());
            Retry retry = retryRegistry.retry(config.getRetryName());
            RateLimiter rl = rateLimiterRegistry.rateLimiter(config.getRateLimiterName());
            
            return chain.filter(exchange)
                    .transformDeferred(RateLimiterOperator.of(rl))
                    .transformDeferred(io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator.of(cb))
                    .transformDeferred(RetryOperator.of(retry))
                    .onErrorResume(throwable -> fallback(exchange, throwable));
        };
    }

    private Mono<Void> fallback(ServerWebExchange exchange, Throwable throwable) {
        String path = exchange.getRequest().getPath().toString();
        String service = resolveServiceFromPath(path);

        String failureType = resolveFailureType(throwable);
        String message = getRootCauseMessage(throwable); // This avoids nested escape chars

        String json = String.format(
            "{\"service\":\"%s\", \"failureType\":\"%s\", \"message\":\"%s\"}",
            service,
            failureType,
            message.replace("\"", "'")  // Replace inner quotes to prevent escaping
        );

        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(
            Mono.just(exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8)))
        );
    }
    
    private String getRootCauseMessage(Throwable t) {
        Throwable root = t;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root.getMessage();
    }
    
    private String resolveFailureType(Throwable t) {
        Throwable cause = t;
        while (cause != null) {
            String name = cause.getClass().getSimpleName().toLowerCase();

            if (name.contains("circuitbreaker")) return "CircuitBreaker";
            if (name.contains("ratelimit")) return "RateLimiter";
            if (name.contains("retry")) return "Retry";
            if (name.contains("notfound") || name.contains("notsuchservice") || name.contains("instance") || name.contains("loadbalancer")) {
                return "ServiceDiscovery";
            }

            cause = cause.getCause();
        }
        return "Unknown";
    }
    
    private String resolveServiceFromPath(String path) {
        if (path.startsWith("/users")) return "USER-SERVICE";
        if (path.startsWith("/hotels") || path.startsWith("/staffs")) return "HOTEL-SERVICE";
        if (path.startsWith("/ratings")) return "RATING-SERVICE";
        return "UNKNOWN-SERVICE";
    }

    public static class Config {
        private String circuitBreakerName;
        private String retryName;
        private String rateLimiterName;

        public String getCircuitBreakerName() {
            return circuitBreakerName;
        }

        public void setCircuitBreakerName(String circuitBreakerName) {
            this.circuitBreakerName = circuitBreakerName;
        }

        public String getRetryName() {
            return retryName;
        }

        public void setRetryName(String retryName) {
            this.retryName = retryName;
        }

        public String getRateLimiterName() {
            return rateLimiterName;
        }

        public void setRateLimiterName(String rateLimiterName) {
            this.rateLimiterName = rateLimiterName;
        }
    }
}
