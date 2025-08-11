package com.lcwd.user.service.aop;

import com.lcwd.user.service.annotation.ResilientEndpoint;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Aspect
@Component
@RequiredArgsConstructor
public class ResilienceAspect {

    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RateLimiterRegistry rateLimiterRegistry;

    @Around("@annotation(resilientEndpoint)")
    public Object handleResilience(ProceedingJoinPoint joinPoint, ResilientEndpoint resilientEndpoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String retryName = resilientEndpoint.retry();
        String circuitBreakerName = resilientEndpoint.circuitBreaker();
        String rateLimiterName = resilientEndpoint.rateLimiter();
        String fallbackMethodName = resilientEndpoint.fallbackMethod();

        Retry retry = retryRegistry.retry(retryName);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(rateLimiterName);

        Callable<Object> decoratedCallable = Retry.decorateCallable(retry,
                CircuitBreaker.decorateCallable(circuitBreaker,
                        RateLimiter.decorateCallable(rateLimiter, () -> {
                            try {
                                return joinPoint.proceed();
                            } catch (Throwable ex) {
                                throw new RuntimeException(ex);
                            }
                        })
                )
        );

        try {
            return decoratedCallable.call();
        } catch (Exception ex) {
            try {
                // Build parameter types with Exception at the end
                Class<?>[] originalParams = signature.getParameterTypes();
                Class<?>[] fallbackParams = new Class<?>[originalParams.length + 1];
                System.arraycopy(originalParams, 0, fallbackParams, 0, originalParams.length);
                fallbackParams[originalParams.length] = Exception.class;

                // Find fallback method
                Method fallbackMethod = joinPoint.getTarget().getClass()
                        .getDeclaredMethod(fallbackMethodName, fallbackParams);
                fallbackMethod.setAccessible(true);

                // Build args + exception
                Object[] args = joinPoint.getArgs();
                Object[] fallbackArgs = new Object[args.length + 1];
                System.arraycopy(args, 0, fallbackArgs, 0, args.length);
                fallbackArgs[args.length] = ex;

                return fallbackMethod.invoke(joinPoint.getTarget(), fallbackArgs);
            } catch (NoSuchMethodException noMethod) {
                throw new RuntimeException("Fallback method not found: " + fallbackMethodName, noMethod);
            } catch (Exception fallbackEx) {
                throw new RuntimeException("Error invoking fallback method: " + fallbackMethodName, fallbackEx);
            }
        }
    }
}
