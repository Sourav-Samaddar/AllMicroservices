package com.lcwd.rabbitconsumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.lcwd.rabbitconsumer.config.RabbitMQConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageListener {

    @RabbitListener(queues = RabbitMQConfig.SIMPLE_QUEUE)
    public void listenSimpleQueue(String message) {
    	log.debug("Consumer 1 received (Simple): " + message);
        System.out.println("Consumer 1 received (Simple): " + message);
    }

    @RabbitListener(queues = RabbitMQConfig.FANOUT_QUEUE_1)
    public void listenFanoutQueue1(String message) {
    	log.debug("Consumer 1 received (Fanout Queue 1): " + message);
        System.out.println("Consumer 1 received (Fanout Queue 1): " + message);
    }
    
    @RabbitListener(queues = RabbitMQConfig.FANOUT_QUEUE_2)
    public void listenFanoutQueue2(String message) {
    	log.debug("Consumer 1 received (Fanout Queue 2): " + message);
        System.out.println("Consumer 1 received (Fanout Queue 2): " + message);
    }
}
