package com.lcwd.user.service.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        template.header("Authorization", "Bearer " + "asdgagr34654gds");
        System.out.println("********Inside FeignClientInterceptor******");

    }
}
