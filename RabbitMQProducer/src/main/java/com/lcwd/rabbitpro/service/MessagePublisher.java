package com.lcwd.rabbitpro.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.lcwd.rabbitpro.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendToSimpleQueue(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.SIMPLE_QUEUE, message);
    }

    public void sendToFanout(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", message);
    }
}