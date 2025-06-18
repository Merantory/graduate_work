package com.tyunin.sso.service;

import com.tyunin.sso.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

	List<User> getAll();
	Optional<User> getUserByEmail(String email);

	Optional<User> getUser(long id);

	Optional<User> getUserByName(String name);

	boolean register(User user);

	User update(User user);

	User delete(long id);

	User changeRole(User user);
}
