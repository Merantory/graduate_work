package com.tyunin.sso.controller;

import com.tyunin.sso.dto.RegisterUserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/register-page")
	public String registerPage(Model model) {
		model.addAttribute("user", new RegisterUserDto());
		return "register";
	}

}
