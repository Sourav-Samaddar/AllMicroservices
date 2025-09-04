package com.lcwd.logtest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("testlog")
@Slf4j
public class BasicLogController {

	@GetMapping("/test")
	public String testLog() {
		log.debug("Helloo ******testLog*******");
		return "This returns value testLog";
	}
	
	@GetMapping("/test/{val}")
	public String testLogWithPathVar(@PathVariable(name = "val") String val) {
		log.debug("Helloo ******testLogWithPathVar updated*******:"+val);
		return "This returns value testLogWithPathVar test:"+val;
	}
}
