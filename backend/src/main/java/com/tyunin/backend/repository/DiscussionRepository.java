package com.tyunin.backend.repository;

import com.tyunin.backend.exception.DiscussionCreationFailedException;
import com.tyunin.backend.model.Discussion;
import com.tyunin.backend.repository.mapper.DiscussionRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class DiscussionRepository {

	private final String GET_ALL = "SELECT * FROM \"discussion\"";
	private final String GET_BY_ID = "SELECT * FROM \"discussion\" WHERE id=?";
	private final String SAVE_DISCUSSION = "INSERT INTO \"discussion\"(user_id, title, content, pinned_at) VALUES(?, ?, ?, ?)";
	private final JdbcTemplate jdbc;

	@Autowired
	public DiscussionRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public List<Discussion> getAll() {
		List<Discussion> discussions = jdbc.query(GET_ALL, new DiscussionRowMapper());
		return discussions;
	}

	public Optional<Discussion> getById(long id) {
		Optional<Discussion> discussionOptional;
		try {
			discussionOptional = Optional.ofNullable(jdbc.queryForObject(GET_BY_ID, new DiscussionRowMapper(), id));
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			discussionOptional = Optional.empty();
		}
		return discussionOptional;
	}

	public boolean save(Discussion discussion) {
		boolean isSaved;
		try {
			isSaved = jdbc.update(SAVE_DISCUSSION,
					discussion.getUserId(),
					discussion.getTitle(),
					discussion.getContent(),
					discussion.getPinnedAt() != null ? Timestamp.from(discussion.getPinnedAt()) : null
			) != 0;
		} catch (DataAccessException exception) {
			throw new DiscussionCreationFailedException(exception.getMessage());
		}
		return isSaved;
	}


}
