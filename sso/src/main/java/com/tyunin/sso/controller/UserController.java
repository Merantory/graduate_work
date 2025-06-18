package com.tyunin.sso.controller;

import com.tyunin.sso.dto.UserDto;
import com.tyunin.sso.dto.mapper.UserMapper;
import com.tyunin.sso.model.User;
import com.tyunin.sso.service.UserService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

	private final UserService userService;
	private final UserMapper userMapper;

	@Autowired
	public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@GetMapping("/api/userinfo")
	public Map<String, Object> getUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> userInfo = new HashMap<>();

		if (authentication != null && authentication.isAuthenticated()) {
			userInfo.put("username", authentication.getName());
			userInfo.put("authenticated", true);

			if (authentication.getPrincipal() instanceof OAuth2User) {
				OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
				userInfo.put("attributes", oauthUser.getAttributes());
			}
		} else {
			userInfo.put("authenticated", false);
		}

		return userInfo;
	}

	@GetMapping("/user")
	public ResponseEntity<UserDto> getPublicUserData(@Nullable @RequestParam(value = "id", required = false) Long id,
													 @Nullable @RequestParam(value = "name", required = false) String name) {
		if (id == null && name == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<User> userOptional;
		if (id != null) {
			userOptional = userService.getUser(id);
		} else {
			userOptional = userService.getUserByName(name);
		}
		if (userOptional.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var user = userOptional.get();
		var userDto = userMapper.toDto(user);
		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getUsers() {
		var users = userService.getAll();
		var userDtos = userMapper.toDto(users);
		return ResponseEntity.ok(userDtos);
	}

}
