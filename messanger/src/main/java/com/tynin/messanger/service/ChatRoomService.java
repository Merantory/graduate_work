package com.tynin.messanger.service;

import com.tynin.messanger.model.ChatRoom;

import java.util.Optional;

public interface ChatRoomService {

	Optional<ChatRoom> findByIds(Long user1, Long user2);
	Optional<ChatRoom> save(Long firstIdUser, Long secondIdUser);

}
