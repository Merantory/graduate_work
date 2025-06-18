package com.tyunin.sso.dto;

import lombok.Data;

@Data
public class UserDto {
	private long id;
	private String email;
	private String name;
	private String role;
}
