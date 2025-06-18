package com.tyunin.backend.service.impl;

import com.tyunin.backend.model.Comment;
import com.tyunin.backend.repository.CommentRepository;
import com.tyunin.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final Logger logger;

	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, Logger logger) {
		this.commentRepository = commentRepository;
		this.logger = logger;
	}

	@Override
	public List<Comment> getAllDiscussionComments(long discussionId) {
		return commentRepository.getAllDiscussionComments(discussionId);
	}

	@Override
	public List<Comment> getAllByUserId(long id) {
		return commentRepository.getAllByUserId(id);
	}

	@Override
	@Transactional(isolation = Isolation.DEFAULT)
	public boolean save(Comment comment) {
		boolean saved = commentRepository.save(comment);
		logger.info("Comment saved: " + comment);
		return saved;
	}
}
