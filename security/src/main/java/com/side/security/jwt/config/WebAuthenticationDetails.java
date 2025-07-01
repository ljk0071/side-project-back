package com.side.security.jwt.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class WebAuthenticationDetails extends org.springframework.security.web.authentication.WebAuthenticationDetails {

    private final String userAgent;

    public WebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.userAgent = request.getHeader("User-Agent");
    }

}