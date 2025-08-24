package com.side.security.filter;

import com.side.security.exception.InvalidTokenException;
import com.side.security.exception.NotFoundTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.side.security.constant.FilterConstant.DEFAULT_PACKAGE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityFilterChainExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) {

        try {

            log.info("uri: {} queryString: {} method: {}",
                    request.getRequestURI(),
                    request.getQueryString(),
                    request.getMethod());

            filterChain.doFilter(request, response);
        } catch (Exception e) {

            if (e instanceof ExpiredJwtException) {
                response.addHeader("RTR", "Y");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if (e instanceof NotFoundTokenException) {
                response.addHeader("RTR", "Y");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if (e instanceof InvalidTokenException ite) {
                response.setStatus(ite.isNeedLogin() ? HttpServletResponse.SC_UNAUTHORIZED : HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            response.setStatus(getStatusCodeByExceptionSource(e));
        }
    }

    private int getStatusCodeByExceptionSource(Exception e) {

        log.error("필터 내에서 에러 발생", e);

        if (e.getClass().getPackage().getName().startsWith(DEFAULT_PACKAGE_NAME)) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }

        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}