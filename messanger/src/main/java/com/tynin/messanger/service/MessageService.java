package com.tynin.messanger.service;

import java.util.List;

import com.tynin.messanger.model.ChatRoom;
import com.tynin.messanger.model.Message;
import com.tynin.messanger.model.User;

public interface MessageService {
    ChatRoom getOrCreateChatRoom(User user1, User user2);
    Message saveMessage(String content, User sender, User receiver);
    List<Message> getChatHistory(Long chatRoomId);
}
