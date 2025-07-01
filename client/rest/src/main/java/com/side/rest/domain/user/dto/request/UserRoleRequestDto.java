package com.side.rest.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleRequestDto {

    public interface Insert extends Default {}

    public interface Update extends Default {}

    @Null(groups = Insert.class, message = "등록 시 ID는 null이어야 합니다")
    @NotNull(groups = Update.class, message = "수정 시 ID는 필수입니다")
    @Positive(groups = Update.class, message = "ID는 양수여야 합니다")
    private Long id;

    @NotNull(groups = {Insert.class, Update.class}, message = "역할 ID는 필수입니다")
    @Positive(groups = {Insert.class, Update.class}, message = "역할 ID는 양수여야 합니다")
    private Long roleId;

    @NotNull(groups = {Insert.class, Update.class}, message = "사용자 ID는 필수입니다")
    @Positive(groups = {Insert.class, Update.class}, message = "사용자 ID는 양수여야 합니다")
    private Long userUniqueId;
}