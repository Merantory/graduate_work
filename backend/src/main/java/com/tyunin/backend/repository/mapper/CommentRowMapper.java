package com.tyunin.backend.repository.mapper;

import com.tyunin.backend.model.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CommentRowMapper implements RowMapper<Comment> {
	@Override
	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
		var comment = new Comment();
		comment.setId(rs.getLong("id"));
		comment.setUserId(rs.getLong("user_id"));
		comment.setDiscussionId(rs.getLong("discussion_id"));
		comment.setContent(rs.getString("content"));
		comment.setCreatedAt(rs.getTimestamp("created_at").toInstant());
		Timestamp deletedAt = rs.getTimestamp("deleted_at");
		if (deletedAt != null) {
			comment.setDeletedAt(deletedAt.toInstant());
		}
		return comment;
	}
}
