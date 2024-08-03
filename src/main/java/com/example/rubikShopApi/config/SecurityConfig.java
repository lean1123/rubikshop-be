package com.example.rubikShopApi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.experimental.NonFinal;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@NonFinal
	private final String[] PUBLIC_URL = { "/user/register", "/user/login", "/admin/register", "/admin/login",
			"/auth/introspect", "/auth/logout", "/admin/categories/searchPagination", "/products/searchPagination", "/auth/refresh", "/admin/products" };

	@NonFinal
	@Value("${jwt.SIGNER_KEY}")
	private String SECRET_KEY;

	@Autowired
	private CustomJwtDecoder customJwtDecoder;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authorizeHttpRequests(t -> t.requestMatchers(HttpMethod.GET, PUBLIC_URL).permitAll()
				.requestMatchers(HttpMethod.POST, PUBLIC_URL).permitAll().requestMatchers(HttpMethod.PUT, PUBLIC_URL)
				.permitAll().requestMatchers(HttpMethod.DELETE, PUBLIC_URL).permitAll().anyRequest().authenticated());

		httpSecurity.oauth2ResourceServer(t -> t
				.jwt(cus -> cus.decoder(customJwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter())));

		httpSecurity.csrf(t -> t.disable());

		return httpSecurity.build();
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}

}
