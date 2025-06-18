package com.tyunin.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CommentDto {
	private long id;

	@JsonProperty("user_id")
	private long userId;

	@JsonProperty("discussion_id")
	private long discussionId;

	private String content;

	@JsonProperty("created_at")
	private Instant createdAt;

}
