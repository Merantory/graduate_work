package com.tynin.messanger.repository;

import com.tynin.messanger.exception.ChatRoomCreationFailedException;
import com.tynin.messanger.exception.MessageCreationFailedException;
import com.tynin.messanger.model.Message;
import com.tynin.messanger.repository.mapper.MessageRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MessageRepository {
	private final String GET_BY_ID = "SELECT * FROM \"message\" WHERE id=?";
	private final String GET_BY_CHAT_ROOM = "SELECT * FROM \"message\" WHERE chat_room=?";
	private final String SAVE_MESSAGE_SQL = "INSERT INTO \"message\"(sender, receiver, content, chat_room) VALUES(?, ?, ?, ?)";
	private final JdbcTemplate jdbc;

	@Autowired
	public MessageRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public Optional<Message> getById(long id) {
		Optional<Message> messageOptional;
		try {
			messageOptional = Optional.ofNullable(jdbc.queryForObject(GET_BY_ID, new MessageRowMapper(), id));
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			messageOptional = Optional.empty();
		}
		return messageOptional;
	}

	public List<Message> findByChatRoomId(Long chatRoomId) {
		List<Message> messages = jdbc.query(GET_BY_CHAT_ROOM, new MessageRowMapper(), chatRoomId);
		return messages;
	}

	public long save(Message message) {
		long id;
		try {
			id = jdbc.update(SAVE_MESSAGE_SQL,
					message.getSender().getId(),
					message.getReceiver().getId(),
					message.getContent(),
					message.getChatRoom().getId()
			);
		} catch (DataAccessException exception) {
			throw new MessageCreationFailedException();
		}
		return id;
	}
}
