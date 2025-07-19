package com.side.rest.domain.user.dto.request;

import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @Null(groups = Insert.class, message = "Insert 시 id는 null이어야 합니다")
    private Long uniqueId;
    @NotBlank(message = "사용자 ID는 필수입니다")
    @Size(min = 3, max = 50, message = "사용자 ID는 3-50자 사이여야 합니다")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "사용자 ID는 영문, 숫자, 언더스코어만 사용 가능합니다")
    private String userId;
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 255, message = "비밀번호는 8-255자 사이여야 합니다")
    private String password;
    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 1, max = 50, message = "이름은 1-50자 사이여야 합니다")
    private String name;
    @Email(message = "올바른 이메일 형식이어야 합니다")
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다")
    private String email;
    @NotNull(message = "상태는 필수입니다")
    private UserStatus status;
    @NotNull(message = "타입은 필수입니다")
    private UserType type;
    @Size(max = 255, message = "설명은 255자 이하여야 합니다")
    private String description;

    public interface Insert extends Default {}
}
