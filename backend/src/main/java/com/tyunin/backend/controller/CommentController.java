package com.tyunin.backend.controller;

import com.tyunin.backend.dto.CommentDto;
import com.tyunin.backend.model.Comment;
import com.tyunin.backend.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;
	private final ModelMapper modelMapper;

	@Autowired
	public CommentController(CommentService commentService, ModelMapper modelMapper) {
		this.commentService = commentService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/discussion/{id}")
	public ResponseEntity<List<CommentDto>> getAllByDiscussionId(@PathVariable("id") long id) {
		var comments = commentService.getAllDiscussionComments(id);
		var commentDtos = comments.stream().map(comment -> modelMapper.map(comment, CommentDto.class)).toList();
		return ResponseEntity.ok(commentDtos);
	}

	@PostMapping
	public ResponseEntity<Boolean> createComment(@RequestBody CommentDto commentDto) {
		var comment = modelMapper.map(commentDto, Comment.class);
		return new ResponseEntity<>(commentService.save(comment), HttpStatus.CREATED);
	}
}
