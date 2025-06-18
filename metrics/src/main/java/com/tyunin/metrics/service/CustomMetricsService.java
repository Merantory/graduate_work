package com.tyunin.metrics.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class CustomMetricsService {

	private final Counter customMetricCounter;

	public CustomMetricsService(MeterRegistry meterRegistry) {
		customMetricCounter = Counter.builder("custom_metric_name")
				.description("Description of custom metric")
				.tags("environment", "development")
				.register(meterRegistry);
	}

	public void incrementCustomMetric() {
		customMetricCounter.increment();
	}

	public double getCounterValue() {
		return customMetricCounter.count();
	}
}
