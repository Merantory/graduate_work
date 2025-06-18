package com.tyunin.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

	private static final Logger log = LoggerFactory.getLogger(StatusController.class);


	@GetMapping("/ping")
	public ResponseEntity<String> pong() {
		log.info("Ping-Pong");
		return ResponseEntity.ok("pong");
	}
}
