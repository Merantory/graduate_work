package com.tyunin.metrics.service;

import com.tyunin.metrics.model.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaLogListener {

	private final Logger log = LoggerFactory.getLogger(KafkaLogListener.class);
	private final YandexCloudHttpLoggingService ycLogger;

	public KafkaLogListener(YandexCloudHttpLoggingService ycLogger) {
		this.ycLogger = ycLogger;
	}

	@KafkaListener(topics = "${kafka.topic.log}", groupId = "${kafka.group-id}")
	public void listen(LogEntry logEntry) {
		try {
			ycLogger.log(logEntry);
		} catch (Exception e) {
			log.error(String.format("Error processing log entry: %s", logEntry.getMessage()), "error");
		}
	}
}

