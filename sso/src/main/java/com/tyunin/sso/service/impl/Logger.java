package com.tyunin.sso.service.impl;

import com.tyunin.sso.model.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class Logger {
	private final KafkaTemplate<String, LogEntry> kafkaTemplate;
	private final String topic;

	private final String appName;

	@Autowired
	public Logger(KafkaTemplate<String, LogEntry> kafkaTemplate,
				  @Value("${kafka.topic.log}") String topic,
				  @Value("${spring.application.name}") String appName) {
		this.kafkaTemplate = kafkaTemplate;
		this.topic = topic;
		this.appName = appName;
	}

	private void write(String msg, String level) {
		var logEntry = LogEntry.builder()
				.message(msg)
				.level(level)
				.timestamp(String.valueOf(Instant.now().getEpochSecond()))
				.requestId(String.valueOf(UUID.randomUUID()))
				.application(appName)
				.build();

		kafkaTemplate.send(topic, logEntry);
	}

	public void info(String msg) {
		write(msg, "INFO");
	}

	public void warn(String msg) {
		write(msg, "WARN");
	}

	public void error(String msg) {
		write(msg, "ERROR");
	}

	public void debug(String msg) {
		write(msg, "DEBUG");
	}
}
