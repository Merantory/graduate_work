package com.tynin.messanger.service.impl;

import com.tynin.messanger.model.ChatRoom;
import com.tynin.messanger.model.User;
import com.tynin.messanger.repository.ChatRoomRepository;
import com.tynin.messanger.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;

	@Autowired
	public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
		this.chatRoomRepository = chatRoomRepository;
	}

	@Override
	public Optional<ChatRoom> findByIds(Long firstIdUser, Long secondIdUser) {
		var user1 = new User();
		user1.setId(firstIdUser);
		var user2 = new User();
		user2.setId(secondIdUser);
		return chatRoomRepository.findByUsers(user1, user2);
	}

	@Override
	public Optional<ChatRoom> save(Long firstIdUser, Long secondIdUser) {
		var user1 = new User();
		user1.setId(firstIdUser);
		var user2 = new User();
		user2.setId(secondIdUser);
		var chatRoom = new ChatRoom();
		chatRoom.setUser1(user1);
		chatRoom.setUser2(user2);
		chatRoomRepository.save(chatRoom);
		return findByIds(firstIdUser, secondIdUser);
	}
}
