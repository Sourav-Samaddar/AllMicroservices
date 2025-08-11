package com.lcwd.rabbitconsumer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rabbitConsume")
@Slf4j
public class RabbitController {

	@GetMapping("/test")
	public String testConsumer() {
		log.debug("Rabbit Consumer 1 test");
		return "From Rabbit Consumer 1";
	}
}
