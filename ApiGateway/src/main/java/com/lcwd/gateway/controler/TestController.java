package com.lcwd.gateway.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/apigateway")
public class TestController {

	@GetMapping("/test")
	public String test() {
		log.debug("ApiGateway ***testLog***");
		return "This is Gateway Server";
	}
}
