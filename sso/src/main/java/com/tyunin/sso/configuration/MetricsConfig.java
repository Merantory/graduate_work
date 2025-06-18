package com.tyunin.sso.configuration;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MetricsConfig {

	@Value("${spring.application.name}")
	public String appName;

	@Bean
	public MeterFilter renameNameLabel() {
		return new MeterFilter() {
			@Override
			public Meter.Id map(Meter.Id id) {
				List<Tag> tags = new ArrayList<>();
				for (var tag : id.getTags()) {
					if (tag.getKey().equals("name")) {
						tags.add(Tag.of("mname", tag.getValue()));
					} else {
						tags.add(tag);
					}
				}
				tags.add(Tag.of("application-name", appName));
				return id.replaceTags(tags);
			}
		};
	}
}
