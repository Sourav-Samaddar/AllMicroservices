package com.lcwd.rabbitconsumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.lcwd.rabbitconsumer.config.RabbitMQConfig;

@Service
public class MessageListener {

    @RabbitListener(queues = RabbitMQConfig.FANOUT_QUEUE_3)
    public void listenFanoutQueue1(String message) {
        System.out.println("Consumer 2 received (Fanout Queue 3): " + message);
    }
}
