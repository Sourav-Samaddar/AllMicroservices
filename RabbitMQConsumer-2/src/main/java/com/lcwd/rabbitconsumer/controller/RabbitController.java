package com.lcwd.rabbitconsumer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitConsume")
public class RabbitController {

	@GetMapping("/test")
	public String testConsumer() {
		return "From Rabbit Consumer 2";
	}
}
