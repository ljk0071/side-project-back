package com.side.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.side.security.util.ResponseUtil.createCookie;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class SignOutFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        log.debug("## SignOutFilter doFilterInternal ##");

        if ("POST".equals(request.getMethod())) {


            Cookie cookie = createInvalidCookie(request.getScheme());

            response.addCookie(cookie);
        }
    }

    private Cookie createInvalidCookie(String scheme) {
        return createCookie(
                true,
                scheme,
                AUTHORIZATION,
                null,
                0
        );
    }
}
