package com.tyunin.metrics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

public class StoreMetricsService {

	@Value("${kafka.group-id}")
	private String groupId;


}
