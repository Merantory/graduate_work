package com.tyunin.sso.repository.mapper;

import com.tyunin.sso.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

@Component
public class UserRowMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setRole(rs.getString("role"));
		try {
			user.setDeletedAt((rs.getTimestamp("deleted_at",
							Calendar.getInstance(TimeZone.getTimeZone("UTC")))
					.toInstant()));
		} catch (Exception ignore) {
		}

		return user;
	}
}
