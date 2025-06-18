package com.tyunin.backend.repository.mapper;

import com.tyunin.backend.model.Discussion;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class DiscussionRowMapper implements RowMapper<Discussion> {
	@Override
	public Discussion mapRow(ResultSet rs, int rowNum) throws SQLException {
		var discussion = new Discussion();
		discussion.setId(rs.getLong("id"));
		discussion.setUserId(rs.getLong("user_id"));
		discussion.setTitle(rs.getString("title"));
		discussion.setContent(rs.getString("content"));
		Timestamp pinnedAt = rs.getTimestamp("pinned_at");
		if (pinnedAt != null) {
			discussion.setPinnedAt(pinnedAt.toInstant());
		}

		Timestamp createdAt = rs.getTimestamp("created_at");
		if (createdAt != null) {
			discussion.setCreatedAt(createdAt.toInstant());
		}

		Timestamp deletedAt = rs.getTimestamp("deleted_at");
		if (deletedAt != null) {
			discussion.setDeletedAt(deletedAt.toInstant());
		}

		return discussion;
	}
}
