package com.side.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.side.security.constant.FilterConstant.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public class ResponseUtil {

    public static Cookie createCookie(
            boolean isHttpOnly,
            String key,
            String value,
            String scheme,
            Integer maxAge
    ) {
        Cookie cookie = new Cookie(key, value);

        cookie.setPath(ALL_PATH);
        cookie.setAttribute(SAME_SITE, NONE);
        cookie.setHttpOnly(isHttpOnly);

        cookie.setSecure(true);
//        if (HTTPS.equals(scheme)) {
//            cookie.setSecure(true);
//        }

        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }

        return cookie;
    }

    public static void createLoginSuccessResponse(
            long userUniqueId,
            String userName,
            String csrfToken,
            String refreshToken,
            ObjectMapper objectMapper,
            HttpServletResponse response
    ) throws IOException {

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(APPLICATION_JSON.toString());
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> result = new HashMap<>();
        result.put("csrfToken", csrfToken);
        result.put("refreshToken", refreshToken);
        result.put("userUniqueId", userUniqueId);
        result.put("userName", userName);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(result));
        }
    }
}
