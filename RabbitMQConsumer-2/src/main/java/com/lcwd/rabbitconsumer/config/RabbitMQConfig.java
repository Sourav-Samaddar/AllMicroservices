package com.lcwd.rabbitconsumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FANOUT_EXCHANGE = "fanout.exchange";
    public static final String FANOUT_QUEUE_3 = "fanout.queue.3";
    
    @Bean
    public Queue fanoutQueue3() {
        return new Queue(FANOUT_QUEUE_3);
    }
        
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding3(Queue fanoutQueue3, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange);
    }
       
}