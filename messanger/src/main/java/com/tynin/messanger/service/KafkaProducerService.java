package com.tynin.messanger.service;

import com.tynin.messanger.model.Message;

public interface KafkaProducerService {
    void sendMessage(Message message);
}
