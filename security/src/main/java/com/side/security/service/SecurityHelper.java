package com.side.security.service;

import com.side.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {

    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    public static User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object user = isAuthenticated() ? authentication.getPrincipal() : null;

        if (user instanceof String) {
            user = User.builder().build();
        }

        return (User) user;
    }
}