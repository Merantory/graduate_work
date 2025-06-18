package com.tyunin.backend.repository;

import com.tyunin.backend.exception.CommentCreationFailedException;
import com.tyunin.backend.model.Comment;
import com.tyunin.backend.repository.mapper.CommentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {
	private final String GET_ALL_BY_DISCUSSION_ID = "SELECT * FROM \"comment\" WHERE discussion_id = ?";
	private final String GET_ALL_BY_USER_ID = "SELECT * FROM \"comment\" WHERE user_id = ?";
	private final String SAVE_COMMENT = "INSERT INTO \"comment\"(user_id, discussion_id, content) VALUES(?, ?, ?)";

	private final JdbcTemplate jdbc;

	@Autowired
	public CommentRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public List<Comment> getAllDiscussionComments(long discussionId) {
		List<Comment> comments = jdbc.query(GET_ALL_BY_DISCUSSION_ID, new CommentRowMapper(), discussionId);
		return comments;
	}

	public List<Comment> getAllByUserId(long userId) {
		List<Comment> comments = jdbc.query(GET_ALL_BY_USER_ID, new CommentRowMapper(), userId);
		return comments;
	}

	public boolean save(Comment comment) {
		boolean isSaved;
		try {
			isSaved = jdbc.update(SAVE_COMMENT,
					comment.getUserId(),
					comment.getDiscussionId(),
					comment.getContent()
			) != 0;
		} catch (DataAccessException exception) {
			throw new CommentCreationFailedException(exception.getMessage());
		}
		return isSaved;
	}


}
