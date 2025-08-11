package com.lcwd.rabbitpro.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String SIMPLE_QUEUE = "simple.queue";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    @Bean
    public Queue simpleQueue() {
        return new Queue(SIMPLE_QUEUE);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
}
