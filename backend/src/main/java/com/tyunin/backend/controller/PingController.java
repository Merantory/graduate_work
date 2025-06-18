package com.tyunin.backend.controller;

import com.tyunin.backend.service.impl.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	private final Logger log;

	public PingController(Logger logger) {
		this.log = logger;
	}

	@GetMapping("/ping")
	public ResponseEntity<String> pong() {
		return ResponseEntity.ok("pong");
	}

	@GetMapping("/test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("test string");
	}

}
