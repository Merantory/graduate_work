package com.tyunin.metrics.controller;

import com.tyunin.metrics.service.CustomMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IncrementCounterController {
	private final CustomMetricsService service;

	@GetMapping("/inc")
	public ResponseEntity<String> increment() {
		service.incrementCustomMetric();
		return ResponseEntity.ok(String.valueOf(service.getCounterValue()));
	}
}
