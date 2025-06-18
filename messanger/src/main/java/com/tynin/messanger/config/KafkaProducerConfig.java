package com.tynin.messanger.config;

import com.tynin.messanger.model.Message;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

	@Value("${kafka.bootstrap-server}")
	private String servers;

	@Value("${kafka.sasl.mechanism}")
	private String saslMechanism;

	@Value("${kafka.user}")
	private String user;

	@Value("${kafka.password}")
	private String password;

	@Value("${kafka.ssl.ts-file}")
	private String tsFile;

	@Value("${kafka.ssl.ts-pass}")
	private String tsPass;

	@Bean
	public ProducerFactory<String, Message> producerFactory() {
		String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
		String jaasCfg = String.format(jaasTemplate, user, password);

		Map<String, Object> properties = new HashMap<>();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
		properties.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
		properties.put(SaslConfigs.SASL_JAAS_CONFIG, jaasCfg);
		properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, tsFile);
		properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, tsPass);

		return new DefaultKafkaProducerFactory<>(properties);
	}

	@Bean
	public KafkaTemplate<String, Message> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
