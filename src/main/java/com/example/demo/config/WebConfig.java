package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/api/grafana/**")
						.allowedOrigins("http://localhost:3000", "http://13.209.98.128:3000")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
						.allowCredentials(true).maxAge(3600);

				registry.addMapping("/api/solog/**")
						.allowedOrigins("http://localhost:3000", "http://13.209.98.128:3000")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
						.allowCredentials(true).maxAge(3600);

				registry.addMapping("/api/connect").allowedOrigins("http://localhost:3000", "http://13.209.98.128:3000")
						.allowedMethods("GET", "OPTIONS").allowedHeaders("*").allowCredentials(false).maxAge(3600);
			}
		};
	}
}
