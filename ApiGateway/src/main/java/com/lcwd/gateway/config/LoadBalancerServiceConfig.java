package com.lcwd.gateway.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClients({
    @LoadBalancerClient(name = "RABBIT-CONSUMER", configuration = CustomLoadBalancerConfiguration.class),
    @LoadBalancerClient(name = "USER-SERVICE", configuration = CustomLoadBalancerConfiguration.class),
    @LoadBalancerClient(name = "HOTEL-SERVICE", configuration = CustomLoadBalancerConfiguration.class),
    @LoadBalancerClient(name = "RATING-SERVICE", configuration = CustomLoadBalancerConfiguration.class),
    @LoadBalancerClient(name = "RABBIT-PRODUCER", configuration = CustomLoadBalancerConfiguration.class),
    @LoadBalancerClient(name = "BASIC-LOG", configuration = CustomLoadBalancerConfiguration.class),
})
public class LoadBalancerServiceConfig {

}
