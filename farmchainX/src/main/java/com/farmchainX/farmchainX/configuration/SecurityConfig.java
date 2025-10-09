package com.farmchainX.farmchainX.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		//by default security is given for all endpoints to over come we use security filter chain
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
			http
			.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/api/auth/**").permitAll()
			.anyRequest().authenticated();
			
			return http.build();
		}
		
}
