package com.tynin.messanger.config;

import com.tynin.messanger.model.Message;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
	@Value("${kafka.bootstrap-server}")
	private String servers;

	@Value("${kafka.group-id}")
	private String group;

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
	public ConsumerFactory<String, Message> consumerFactory() {
		String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
		String jaasCfg = String.format(jaasTemplate, user, password);

		Map<String, Object> properties = new HashMap<>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, group);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
		properties.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
		properties.put(SaslConfigs.SASL_JAAS_CONFIG, jaasCfg);
		properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, tsFile);
		properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, tsPass);
		properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		properties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
		properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Message.class);

		return new DefaultKafkaConsumerFactory<>(
				properties,
				new StringDeserializer(),
				new JsonDeserializer<>(Message.class)
		);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
		var factory = new ConcurrentKafkaListenerContainerFactory<String, Message>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}
}
