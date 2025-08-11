package com.lcwd.gateway.controler;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@RestController
public class GatewayFallbackController {

    @RequestMapping("/fallback/user")
    public Mono<Void> userFallback(ServerWebExchange exchange) {
        return writeResponse(exchange, "User Service is unavailable.");
    }

    @RequestMapping("/fallback/hotel")
    public Mono<Void> hotelFallback(ServerWebExchange exchange) {
        return writeResponse(exchange, "Hotel Service is unavailable.");
    }

    @RequestMapping("/fallback/rating")
    public Mono<Void> ratingFallback(ServerWebExchange exchange) {
        return writeResponse(exchange, "Rating Service is unavailable.");
    }
    
    @RequestMapping("/fallback/rabbitProduce")
    public Mono<Void> rabbitMQProducerFallback(ServerWebExchange exchange) {
        return writeResponse(exchange, "Rabbit MQ Producer Service is unavailable.");
    }
    
    @RequestMapping("/fallback/rabbitConsume")
    public Mono<Void> rabbitMQConsumerFallback(ServerWebExchange exchange) {
        return writeResponse(exchange, "Rabbit MQ Consumer Service is unavailable.");
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(message.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
