package com.side.rest.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AuthTokenRequestDto
        (
                @NotBlank(message = "사용자 ID는 필수입니다")
                @Size(min = 3, max = 50, message = "사용자 ID는 3-50자 사이여야 합니다")
                String userId,

                @NotBlank(message = "비밀번호는 필수입니다")
                @Size(min = 1, max = 255, message = "비밀번호는 255자 이하여야 합니다")
                String password,

                @NotNull(message = "토큰 유효 일수는 필수입니다")
                @Positive(message = "토큰 유효 일수는 양수여야 합니다")
                Integer days
        ) {

}
