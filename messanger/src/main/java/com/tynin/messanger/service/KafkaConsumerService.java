package com.tynin.messanger.service;

import com.tynin.messanger.model.Message;

public interface KafkaConsumerService {
    void consumeMessage(Message message);
}
