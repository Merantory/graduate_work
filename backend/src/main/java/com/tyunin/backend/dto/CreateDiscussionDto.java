package com.tyunin.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CreateDiscussionDto {
	@JsonProperty("user_id")
	private long userId;

	private String title;

	private String content;

	@JsonProperty("pinned_at")
	private Instant pinnedAt;

	@JsonProperty("created_at")
	private Instant createdAt;

}
