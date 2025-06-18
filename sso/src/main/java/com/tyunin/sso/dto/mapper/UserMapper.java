package com.tyunin.sso.dto.mapper;

import com.tyunin.sso.dto.RegisterUserDto;
import com.tyunin.sso.dto.UserDto;
import com.tyunin.sso.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

	public User toUser(RegisterUserDto registerUserDto) {
		User user = new User();
		user.setEmail(registerUserDto.getEmail());
		user.setName(registerUserDto.getName());
		user.setPassword(registerUserDto.getPassword());
		user.setRole("USER_ROLE");
		return user;
	}

	public UserDto toDto(User user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setRole(user.getRole());
		return dto;
	}

	public List<UserDto> toDto(List<User> userList) {
		List<UserDto> userDtos = new ArrayList<>();
		for (User user : userList) {
			userDtos.add(toDto(user));
		}
		return userDtos;
	}

}
