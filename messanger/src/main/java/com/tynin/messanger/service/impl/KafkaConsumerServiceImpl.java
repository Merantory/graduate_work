package com.tynin.messanger.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tynin.messanger.model.Message;
import com.tynin.messanger.service.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public KafkaConsumerServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @KafkaListener(topics = "messages", groupId = "${kafka.group-id}")
    public void consumeMessage(Message message) {
        try {
            messagingTemplate.convertAndSendToUser(
                    message.getReceiver().getName(),
                    "/queue/messages",
                    message
            );
        } catch (Exception e) {
        }
    }
}
