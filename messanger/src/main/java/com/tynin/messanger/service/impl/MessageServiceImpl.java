package com.tynin.messanger.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.tynin.messanger.model.ChatRoom;
import com.tynin.messanger.model.Message;
import com.tynin.messanger.model.User;
import com.tynin.messanger.repository.ChatRoomRepository;
import com.tynin.messanger.repository.MessageRepository;
import com.tynin.messanger.service.KafkaProducerService;
import com.tynin.messanger.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              ChatRoomRepository chatRoomRepository,
                              KafkaProducerService kafkaProducerService) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    @Transactional
    public ChatRoom getOrCreateChatRoom(User user1, User user2) {
        Optional<ChatRoom> existingRoomOptional = chatRoomRepository.findByUsers(user1, user2);

        if (existingRoomOptional.isPresent()) {
            var existingRoom = existingRoomOptional.get();
            existingRoom.setUser1(user1);
            existingRoom.setUser2(user2);
            return existingRoom;
        } else {
            ChatRoom newRoom = new ChatRoom();
            newRoom.setUser1(user1);
            newRoom.setUser2(user2);
            chatRoomRepository.save(newRoom);
            return chatRoomRepository.findByUsers(user1, user2).get();
        }
    }

    @Override
    @Transactional
    public Message saveMessage(String content, User sender, User receiver) {
        ChatRoom chatRoom = getOrCreateChatRoom(sender, receiver);
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setChatRoom(chatRoom);
        long id = messageRepository.save(message);
        message.setId(id);
        CompletableFuture.runAsync(() -> {
            message.setSender(sender);
            message.setReceiver(sender);
            kafkaProducerService.sendMessage(message);
        });
        CompletableFuture.runAsync(() -> {
            message.setSender(sender);
            message.setReceiver(receiver);
            kafkaProducerService.sendMessage(message);
        });
        return message;
    }

    @Override
    public List<Message> getChatHistory(Long chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId);
    }
}
