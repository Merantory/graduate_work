package com.tyunin.sso.controller;

import com.tyunin.sso.dto.RegisterUserDto;
import com.tyunin.sso.dto.mapper.UserMapper;
import com.tyunin.sso.exception.UserAlreadyExistException;
import com.tyunin.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
	private final UserService userService;
	private final UserMapper userMapper;

	@Autowired
	public RegistrationController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}
	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerApi(@RequestBody RegisterUserDto registerUserDto) {
		var user = userMapper.toUser(registerUserDto);
		userService.register(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String registerForm(@ModelAttribute RegisterUserDto registerUserDto, RedirectAttributes redirectAttributes) {
		try {
			var user = userMapper.toUser(registerUserDto);
			userService.register(user);
			redirectAttributes.addFlashAttribute("registrationSuccess", true);
			return "redirect:/login";
		} catch (UserAlreadyExistException e) {
			redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует");
			return "redirect:/register-page";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Ошибка при регистрации");
			return "redirect:/register-page";
		}
	}
}