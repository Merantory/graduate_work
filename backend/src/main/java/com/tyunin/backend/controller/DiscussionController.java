package com.tyunin.backend.controller;

import com.tyunin.backend.dto.CreateDiscussionDto;
import com.tyunin.backend.dto.DiscussionDto;
import com.tyunin.backend.model.Discussion;
import com.tyunin.backend.service.DiscussionService;
import com.tyunin.backend.service.impl.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

	private final DiscussionService discussionService;
	private final ModelMapper modelMapper;
	private final Logger logger;

	@Autowired
	public DiscussionController(DiscussionService discussionService, ModelMapper modelMapper, Logger logger) {
		this.discussionService = discussionService;
		this.modelMapper = modelMapper;
		this.logger = logger;
	}

	@GetMapping
	public ResponseEntity<List<DiscussionDto>> getAll() {
		var discussions = discussionService.getAll();
		var disussionDtos = discussions.stream().map(discussion -> modelMapper.map(discussion, DiscussionDto.class)).toList();
		return ResponseEntity.ok(disussionDtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DiscussionDto> getDiscussion(@PathVariable("id") long discussionId) {
		var discussion = discussionService.getById(discussionId);
		var discussionDto = modelMapper.map(discussion, DiscussionDto.class);
		return ResponseEntity.ok(discussionDto);
	}

	@PostMapping
	public ResponseEntity<Boolean> createDiscussion(@RequestBody CreateDiscussionDto createDiscussionDto) {
		var discussion = modelMapper.map(createDiscussionDto, Discussion.class);
		return new ResponseEntity<>(discussionService.save(discussion), HttpStatus.CREATED);
	}


}
