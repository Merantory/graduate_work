package com.tyunin.backend.service;

import com.tyunin.backend.model.Discussion;

import java.util.List;
import java.util.Optional;

public interface DiscussionService {
	List<Discussion> getAll();
	Optional<Discussion> getById(long id);
	boolean save(Discussion discussion);

}
