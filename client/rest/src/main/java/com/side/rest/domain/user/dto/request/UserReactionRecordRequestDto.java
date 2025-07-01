package com.side.rest.domain.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReactionRecordRequestDto {

    @Null(message = "등록 시 ID는 null이어야 합니다")
    private Long id;

    @NotNull(message = "사용자 고유 ID는 필수입니다")
    @Positive(message = "사용자 고유 ID는 양수여야 합니다")
    private Long userUniqueId;

    @NotBlank(message = "대상 타입은 필수입니다")
    @Size(max = 20, message = "대상 타입은 20자를 초과할 수 없습니다")
    private String targetType;

    @NotNull(message = "대상 ID는 필수입니다")
    @Positive(message = "대상 ID는 양수여야 합니다")
    private Long targetId;

    @NotBlank(message = "반응 타입은 필수입니다")
    @Size(max = 10, message = "반응 타입은 10자를 초과할 수 없습니다")
    private String reactionType;

    @NotNull(message = "삭제 여부는 필수입니다")
    private Boolean isDeleted;
}