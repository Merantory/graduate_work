package com.tynin.messanger.repository.mapper;

import com.tynin.messanger.model.ChatRoom;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatRoomRowMapper implements RowMapper<ChatRoom> {
	@Override
	public ChatRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
		var chatRoom = new ChatRoom();
		chatRoom.setId(rs.getLong("id"));
		chatRoom.setCreatedAt(rs.getDate("created_at"));
		return chatRoom;
	}
}
