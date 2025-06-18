package com.tyunin.backend.service.impl;

import com.tyunin.backend.model.Discussion;
import com.tyunin.backend.repository.DiscussionRepository;
import com.tyunin.backend.service.DiscussionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DiscussionServiceImpl implements DiscussionService {

	private final DiscussionRepository discussionRepository;
	private final Logger logger;

	public DiscussionServiceImpl(DiscussionRepository discussionRepository, Logger logger) {
		this.discussionRepository = discussionRepository;
		this.logger = logger;
	}

	@Override
	public List<Discussion> getAll() {
		return discussionRepository.getAll();
	}

	@Override
	public Optional<Discussion> getById(long id) {
		return discussionRepository.getById(id);
	}

	@Override
	@Transactional
	public boolean save(Discussion discussion) {
		boolean saved = discussionRepository.save(discussion);
		logger.info("Discussion saved: " + discussion);
		return saved;
	}
}
