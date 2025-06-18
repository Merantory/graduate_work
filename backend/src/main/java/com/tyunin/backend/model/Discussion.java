package com.tyunin.backend.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Discussion {
	private long id;
	private long userId;
	private String title;
	private String content;
	private Instant pinnedAt;
	private Instant createdAt;
	private Instant deletedAt;
}
