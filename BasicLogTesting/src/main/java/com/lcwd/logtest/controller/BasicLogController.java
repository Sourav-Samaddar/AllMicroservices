package com.lcwd.logtest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("testlog")
@Slf4j
public class BasicLogController {

	@GetMapping("/test")
	public String testLog() {
		log.debug("Helloo *************");
		return "This returns value 11";
	}
}
