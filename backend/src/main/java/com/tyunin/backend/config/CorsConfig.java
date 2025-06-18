package com.tyunin.backend.config;

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

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		var config = new CorsConfiguration();
		config.setAllowCredentials(true);

		config.addAllowedOrigin(String.format("%s:%s,%s:%s,%s:%s", frontedBaseUrl, frontedPort, ssoBaseUrl, ssoPort, "localhost","8080"));
		config.addAllowedHeader(CorsConfiguration.ALL);
		config.addExposedHeader(CorsConfiguration.ALL);
		config.addAllowedMethod(CorsConfiguration.ALL);

		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean.getFilter();
	}

}
