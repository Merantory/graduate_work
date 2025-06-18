package com.tyunin.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {
	private String message;
	private String level;
	private String timestamp;
	private String requestId;
	private String application;
}
