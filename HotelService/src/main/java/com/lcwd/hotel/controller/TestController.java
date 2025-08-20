package com.lcwd.hotel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/hoteltest")
public class TestController {

	@GetMapping("/test")
	public String test() {
		log.debug("HotelService ***testLog***");
		return "This is Hotel Server";
	}
}
