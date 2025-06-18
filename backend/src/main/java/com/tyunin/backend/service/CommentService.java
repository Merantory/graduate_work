package com.tyunin.backend.service;

import com.tyunin.backend.model.Comment;

import java.util.List;

public interface CommentService {

	List<Comment> getAllDiscussionComments(long discussionId);
	List<Comment> getAllByUserId(long id);
	boolean save(Comment comment);

}
