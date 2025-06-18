package com.tynin.messanger.service.impl;

import com.tynin.messanger.model.User;
import com.tynin.messanger.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Value("${cors.sso.base-url}")
	private String ssoBaseUrl;
	@Value("${cors.sso.port}")
	private String ssoPort;


	@Override
	public Optional<User> findById(long id) {
		var restClient = RestClient.create();
		var baseUrl = ssoBaseUrl + ":" + ssoPort;
		var response = restClient.get()
				.uri(baseUrl + "/user?id={id}", id)
				.retrieve().toEntity(User.class);
		return response.getStatusCode().is2xxSuccessful() ? Optional.of(response.getBody()) : Optional.empty();
	}


	@Override
	public Optional<User> findByName(String name) {
		var restClient = RestClient.create();
		var baseUrl = ssoBaseUrl + ":" + ssoPort;
		var response = restClient.get()
				.uri(baseUrl + "/user?name={name}", name)
				.retrieve().toEntity(User.class);
		return response.getStatusCode().is2xxSuccessful() ? Optional.of(response.getBody()) : Optional.empty();
	}
}
