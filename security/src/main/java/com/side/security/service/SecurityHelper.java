package com.side.security.service;

import org.springframework.security.core.context.SecurityContextHolder;

import com.side.domain.model.User;

public class SecurityHelper {

	public static User getAuthenticatedUser() {

		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (user instanceof String) {
			user = User.builder().build();
		}

		return (User)user;
	}
}