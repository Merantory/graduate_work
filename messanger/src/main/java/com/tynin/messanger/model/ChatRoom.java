package com.tynin.messanger.model;

import lombok.Data;

import java.util.Date;

@Data
public class ChatRoom {
    private long id;
    private User user1;
    private User user2;
    private Date createdAt = new Date();
}
