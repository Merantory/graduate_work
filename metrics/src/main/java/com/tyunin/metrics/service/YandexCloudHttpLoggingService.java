package com.tyunin.metrics.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.tyunin.metrics.model.LogEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class YandexCloudHttpLoggingService extends AppenderBase<ILoggingEvent> {

	private final RestTemplate restTemplate;
	private final String endpoint = "http://51.250.107.192:22132/write";
	private final String appName;

	public YandexCloudHttpLoggingService(@Value("${spring.application.name}") String appName) {
		this.restTemplate = new RestTemplate();
		this.appName = appName;
	}

	@Override
	protected void append(ILoggingEvent event) {
		var logEntry = LogEntry.builder()
				.message(event.getFormattedMessage())
				.level(event.getLevel().toString())
				.timestamp(String.valueOf(event.getTimeStamp()))
				.requestId(String.valueOf(UUID.randomUUID()))
				.application(appName)
				.build();
		log(logEntry);
	}

	public void log(LogEntry logEntry) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Object> body = new HashMap<>();

			body.put("message", logEntry.getMessage());
			body.put("level", logEntry.getLevel() != null ? logEntry.getLevel().toUpperCase() : "INFO");
			body.put("timestamp", logEntry.getTimestamp() != null ? logEntry.getTimestamp() : String.valueOf(System.currentTimeMillis()));
			body.put("requestId", logEntry.getRequestId() != null ? logEntry.getRequestId() : "null");
			body.put("application", logEntry.getApplication() != null ? logEntry.getApplication() : "null");

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
			restTemplate.exchange(
					endpoint,
					HttpMethod.POST,
					request,
					String.class
			);
		} catch (Exception e) {
			log(LogEntry.builder()
					.message(String.format("Error sending log: %s", e.getMessage()))
					.level("ERROR")
					.application(appName)
					.build()
			);
		}
	}
}
