package com.tynin.messanger.repository;

import com.tynin.messanger.exception.ChatRoomCreationFailedException;
import com.tynin.messanger.model.ChatRoom;
import com.tynin.messanger.model.User;
import com.tynin.messanger.repository.mapper.ChatRoomRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChatRoomRepository {

	private final String GET_CHAT_ROOM_BY_USER_SQL = "SELECT * FROM \"chat_room\" WHERE (user1=? AND user2=?) OR (user1=? AND user2=?)";
	private final String SAVE_CHAT_ROOM_SQL = "INSERT INTO \"chat_room\"(user1, user2) VALUES(?, ?)";
	private final JdbcTemplate jdbc;

	public ChatRoomRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public Optional<ChatRoom> findByUsers(User user1, User user2) {
		Optional<ChatRoom> chatRoomOptional;
		try {
			chatRoomOptional = Optional.ofNullable(jdbc.queryForObject(GET_CHAT_ROOM_BY_USER_SQL, new ChatRoomRowMapper(),
					user1.getId(), user2.getId(), user2.getId(), user1.getId()));
		} catch (EmptyResultDataAccessException emptyException) {
			chatRoomOptional = Optional.empty();
		}
		return chatRoomOptional;
	}

	public boolean save(ChatRoom newRoom) {
		boolean isSaved;
		try {
			isSaved = (jdbc.update(SAVE_CHAT_ROOM_SQL, newRoom.getUser1().getId(), newRoom.getUser2().getId())) != 0;
		} catch (DataAccessException exception) {
			throw new ChatRoomCreationFailedException();
		}
		return isSaved;
	}
}
