package com.tynin.messanger.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tynin.messanger.model.Message;
import com.tynin.messanger.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final String topic;
    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Autowired
    public KafkaProducerServiceImpl(KafkaTemplate<String, Message> kafkaTemplate,
                                    @Value("${kafka.topic.messages}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void sendMessage(Message message) {
        kafkaTemplate.send(topic, String.valueOf(message.getReceiver().getId()), message);
    }
}
