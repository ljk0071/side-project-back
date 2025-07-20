package com.side.security.service;

import com.side.domain.model.User;
import com.side.security.exception.NotLogInException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {

    public static boolean isAuthenticated() {

        return (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String principal)) ||
                !"anonymousUser".equals(principal);
    }

    public static User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object user = authentication.getPrincipal();

        // 비로그인 상태일 경우 anonymous가 출력
        if (user instanceof String) {
            throw new NotLogInException("로그인 상태가 아닙니다.");
        }

        return (User) user;
    }

    public static long getAuthenticatedUserUniqueId() {
        return getAuthenticatedUser().uniqueId();
    }
}