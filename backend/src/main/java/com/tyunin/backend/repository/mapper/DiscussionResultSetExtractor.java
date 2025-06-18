package com.tyunin.backend.repository.mapper;

import com.tyunin.backend.model.Discussion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscussionResultSetExtractor implements ResultSetExtractor<List<Discussion>> {
	@Override
	public List<Discussion> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Long, Discussion> idDiscussionMap = new HashMap<>();
		while (rs.next()) {
			Long id = rs.getLong("id");
			if (!idDiscussionMap.containsKey(id)) {
				var discussion = new Discussion();
				discussion.setId(id);
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

				idDiscussionMap.put(id, discussion);
			}
		}
		return new ArrayList<>(idDiscussionMap.values());
	}
}
