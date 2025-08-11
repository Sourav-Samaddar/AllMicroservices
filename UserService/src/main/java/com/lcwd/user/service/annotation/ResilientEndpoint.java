package com.lcwd.user.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResilientEndpoint {
    String retry() default "ratingHotelService";
    String circuitBreaker() default "ratingHotelBreaker";
    String rateLimiter() default "userRateLimiter";
    String fallbackMethod() default "ratingHotelFallback";
}
