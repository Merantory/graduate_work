package com.tynin.messanger.repository.mapper;

import com.tynin.messanger.model.Message;
import com.tynin.messanger.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRowMapper implements RowMapper<Message> {
	@Override
	public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
		Message message = new Message();
		message.setId(rs.getLong("id"));
		var sender = new User();
		sender.setId(rs.getLong("sender"));
		message.setSender(sender);
		var receiver = new User();
		receiver.setId(rs.getLong("receiver"));
		message.setSender(receiver);
		message.setContent(rs.getString("content"));
		message.setSentAt(rs.getTimestamp("sent_at"));
		return message;
	}
}
