package com.tynin.messanger.service;

import com.tynin.messanger.model.User;

import java.util.Optional;

public interface UserService {

	Optional<User> findById(long id);
	Optional<User> findByName(String name);
}
