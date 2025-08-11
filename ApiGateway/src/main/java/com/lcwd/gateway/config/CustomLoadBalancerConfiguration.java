package com.lcwd.gateway.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CustomLoadBalancerConfiguration {
    
    @Bean
    public ReactorLoadBalancer<ServiceInstance> customLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        
        String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
                
        return new CustomLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class),
                serviceId);
    }
        
}
