package com.tyunin.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionDto {
	private long id;

	@JsonProperty("user_id")
	private long userId;

	private String title;

	private String content;

	@JsonProperty("pinned_at")
	private Instant pinnedAt;

	@JsonProperty("created_at")
	private Instant createdAt;

	@JsonProperty("deleted_at")
	private Instant deletedAt;

}
