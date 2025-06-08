package com.side.security.jwt.config;

import jakarta.servlet.http.HttpServletRequest;

public class AuthenticationDetailsSource implements
	org.springframework.security.authentication.AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

	@Override
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		return new WebAuthenticationDetails(context);
	}
}