package com.lcwd.user.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/usertest")
public class TestController {

	@GetMapping("/test")
	public String test() {
		log.debug("UserService ***testLog***");
		return "This is User Server";
	}
}
