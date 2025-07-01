package com.side.rest.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Builder
public class LoginAttemptLogResponseDto {

    private Long id;
    private String userId;
    private String ipAddress;
    private String userAgent;
    private Boolean isSucceeded;
    private String failureReason;
    private Instant createdAt;
}