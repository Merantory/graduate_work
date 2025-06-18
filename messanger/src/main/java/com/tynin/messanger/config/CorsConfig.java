package com.tynin.messanger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Value("${cors.frontend.base-url}")
	private String frontedBaseUrl;

	@Value("${cors.frontend.port}")
	private String frontedPort;

	@Value("${cors.sso.base-url}")
	private String ssoBaseUrl;

	@Value("${cors.sso.port}")
	private String ssoPort;

	@Value("${cors.backend.base-url}")
	private String backendBaseUrl;

	@Value("${cors.backend.port}")
	private String backendPort;

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		var config = new CorsConfiguration();
		config.setAllowCredentials(true);

		config.addAllowedOrigin(String.format("%s:%s", frontedBaseUrl, frontedPort));
		config.addAllowedOrigin(String.format("%s:%s", ssoBaseUrl, ssoPort));
		config.addAllowedOrigin(String.format("%s:%s", backendBaseUrl, backendPort));
		config.addAllowedHeader(CorsConfiguration.ALL);
		config.addExposedHeader(CorsConfiguration.ALL);
		config.addAllowedMethod(CorsConfiguration.ALL);

		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean.getFilter();
	}

}
