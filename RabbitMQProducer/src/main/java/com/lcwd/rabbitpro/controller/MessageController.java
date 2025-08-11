package com.lcwd.rabbitpro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lcwd.rabbitpro.service.MessagePublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/publish")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessagePublisher publisher;

    @PostMapping("/simple")
    public ResponseEntity<String> sendToSimple(@RequestBody String message) {
    	log.debug("Helloo sendToSimple*************:"+message);
        publisher.sendToSimpleQueue(message);
        return ResponseEntity.ok("Message sent to simple queue");
    }

    @PostMapping("/fanout")
    public ResponseEntity<String> sendToFanout(@RequestBody String message) {
    	log.debug("Helloo sendToFanout*************:"+message);
        publisher.sendToFanout(message);
        return ResponseEntity.ok("Message sent to fanout exchange");
    }
}