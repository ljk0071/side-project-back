package com.side.rest.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttemptLogRequestDto {

    @Null(message = "등록 시 ID는 null이어야 합니다")
    private Long id;

    @Size(max = 50, message = "사용자 ID는 50자를 초과할 수 없습니다")
    private String userId;

    @NotBlank(message = "IP 주소는 필수입니다")
    @Size(max = 45, message = "IP 주소는 45자를 초과할 수 없습니다")
    private String ipAddress;

    @Size(max = 255, message = "User Agent는 255자를 초과할 수 없습니다")
    private String userAgent;
}