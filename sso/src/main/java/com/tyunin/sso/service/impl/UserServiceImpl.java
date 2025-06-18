package com.tyunin.sso.service.impl;

import com.tyunin.sso.exception.UserAlreadyExistException;
import com.tyunin.sso.exception.UserNotFoundException;
import com.tyunin.sso.model.User;
import com.tyunin.sso.repository.UserRepository;
import com.tyunin.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.getByEmail(username);
		if (userOptional.isEmpty()) {
			throw new BadCredentialsException("User with this email not found");
		}
		return userOptional.get();
	}

	@Override
	public List<User> getAll() {
		List<User> users = userRepository.getAll();
		return users;
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepository.getByEmail(email);
	}

	@Override
	public Optional<User> getUser(long id) {
		return userRepository.getUser(id);
	}

	@Override
	public Optional<User> getUserByName(String name) {
		return userRepository.getUserByName(name);
	}

	@Override
	@Transactional
	public boolean register(User user) {
		if (isExistUserWithEmail(user)) {
			throw new UserAlreadyExistException(String.format("User with this email %s already exist.",
					user.getEmail()));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User update(User user) {
		if (!isExistUser(user)) {
			throw new UserNotFoundException(String.format("User with id %d not found.", user.getId()));
		}
		userRepository.updateUserInfo(user);
		Optional<User> updatedUserOptional = getUser(user.getId());
		return updatedUserOptional.get();
	}

	@Override
	@Transactional
	public User delete(long id) {
		Optional<User> userOptional = getUser(id);
		if (userOptional.isEmpty()) {
			throw new UserNotFoundException(String.format("User with id %d not found.", id));
		}
		userRepository.delete(id);
		return userOptional.get();
	}

	@Override
	@Transactional
	public User changeRole(User user) {
		if (!isExistUser(user.getId())) {
			throw new UserNotFoundException(String.format("User with id %d not found.", user.getId()));
		}
		Boolean isChanged = userRepository.changeRole(user);
		user = getUser(user.getId()).get();
		return user;
	}

	private Boolean isExistUserWithEmail(User user) {
		return isExistUserWithEmail(user.getEmail());
	}

	private Boolean isExistUserWithEmail(String email) {
		return userRepository.getByEmail(email).isPresent();
	}

	private Boolean isExistUser(User user) {
		return isExistUser(user.getId());
	}

	private Boolean isExistUser(Long id) {
		return userRepository.getUser(id).isPresent();
	}
}
