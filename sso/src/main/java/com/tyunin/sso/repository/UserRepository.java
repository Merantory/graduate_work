package com.tyunin.sso.repository;

import com.tyunin.sso.exception.UserCreationFailedException;
import com.tyunin.sso.exception.UserDeleteFailedException;
import com.tyunin.sso.exception.UserUpdateInfoFailedException;
import com.tyunin.sso.exception.UserUpdateRoleFailedException;
import com.tyunin.sso.model.User;
import com.tyunin.sso.repository.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
	private final String GET_ALL = "SELECT * FROM \"user\"";
	private final String GET_USER_BY_EMAIL_SQL = "SELECT * FROM \"user\" WHERE email=?";
	private final String GET_USER_BY_ID_SQL = "SELECT * FROM \"user\" WHERE id=?";
	private final String GET_USER_BY_NAME_SQL = "SELECT * FROM \"user\" WHERE name=?";
	private final String SAVE_USER_SQL = "INSERT INTO \"user\"(email, password, name, role) VALUES(?, ?, ?, ?)";
	private final String UPDATE_USER_SQL = "UPDATE \"user\" SET name=? WHERE id=?";
	private final String DELETE_USER_SQL = "UPDATE \"user\" SET deleted_at = ? WHERE id=?";
	private final String CHANGE_USER_ROLE_SQL = "UPDATE \"user\" SET role=? WHERE id=?";
	private final JdbcTemplate jdbc;

	@Autowired
	public UserRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public List<User> getAll() {
		List<User> users = jdbc.query(GET_ALL, new UserRowMapper());
		return users;
	}

	public Optional<User> getByEmail(String email) {
		Optional<User> userOptional;
		try {
			userOptional = Optional.ofNullable(jdbc.queryForObject(GET_USER_BY_EMAIL_SQL, new UserRowMapper(), email));
		} catch (EmptyResultDataAccessException emptyException) {
			userOptional = Optional.empty();
		}
		return userOptional;
	}

	public Optional<User> getUser(Long id) {
		Optional<User> userOptional;
		try {
			userOptional = Optional.ofNullable(jdbc.queryForObject(GET_USER_BY_ID_SQL, new UserRowMapper(), id));
		} catch (EmptyResultDataAccessException emptyException) {
			userOptional = Optional.empty();
		}
		return userOptional;
	}

	public Boolean save(User user) {
		boolean isSaved;
		try {
			isSaved = (jdbc.update(SAVE_USER_SQL, user.getEmail(), user.getPassword(),
					user.getName(), user.getRole())) != 0;
		} catch (DataAccessException exception) {
			throw new UserCreationFailedException();
		}
		return isSaved;
	}

	public Boolean updateUserInfo(User user) {
		Boolean isUpdated = false;
		try {
			isUpdated = (jdbc.update(UPDATE_USER_SQL, user.getName(), user.getId())) != 0;
		} catch (DataAccessException exception) {
			throw new UserUpdateInfoFailedException();
		}
		return isUpdated;
	}

	public Boolean delete(Long id) {
		Boolean isDeleted = false;
		try {
			isDeleted = (jdbc.update(DELETE_USER_SQL, Timestamp.from(Instant.now()), id)) != 0;
		} catch (DataAccessException exception) {
			throw new UserDeleteFailedException();
		}
		return isDeleted;
	}

	public Boolean changeRole(User user) {
		Boolean isChanged = false;
		try {
			isChanged = (jdbc.update(CHANGE_USER_ROLE_SQL, user.getRole(), user.getId())) != 0;
		} catch (DataAccessException exception) {
			throw new UserUpdateRoleFailedException();
		}
		return isChanged;
	}

	public Optional<User> getUserByName(String name) {
		Optional<User> userOptional;
		try {
			userOptional = Optional.ofNullable(jdbc.queryForObject(GET_USER_BY_NAME_SQL, new UserRowMapper(), name));
		} catch (EmptyResultDataAccessException emptyException) {
			userOptional = Optional.empty();
		}
		return userOptional;
	}
}
