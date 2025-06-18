package com.tyunin.backend.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
	private long id;
	private long userId;
	private long discussionId;
	private String content;
	private Instant createdAt;
	private Instant deletedAt;
}
