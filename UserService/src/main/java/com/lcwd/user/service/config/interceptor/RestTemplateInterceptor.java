package com.lcwd.user.service.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {


    private Logger logger= LoggerFactory.getLogger(RestTemplateInterceptor.class);
    

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = "sagasggsdf353df";

        logger.info("Rest Template interceptor: Token :  {} ",token);

        request.getHeaders().add("Authorization","Bearer "+token);
        
        System.out.println("********Inside RestTemplateInterceptor******");
        return execution.execute(request,body);
    }
}
