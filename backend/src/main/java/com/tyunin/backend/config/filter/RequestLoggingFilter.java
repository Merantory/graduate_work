package com.tyunin.backend.config.filter;

import com.tyunin.backend.service.impl.Logger;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter implements Filter {

	private final Logger logger;

	@Autowired
	public RequestLoggingFilter(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		chain.doFilter(request, response);

		logger.info("Incoming request | Method: " + httpRequest.getMethod() +
				" | URI: " + httpRequest.getRequestURI() +
				" | Response status: " + httpResponse.getStatus());
	}
}