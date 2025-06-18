package com.tynin.messanger.model;

import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
public class Message {
	private long id;
	private User sender;
	private User receiver;
	private String content;
	private ChatRoom chatRoom;
	private Date sentAt = new Date();
}