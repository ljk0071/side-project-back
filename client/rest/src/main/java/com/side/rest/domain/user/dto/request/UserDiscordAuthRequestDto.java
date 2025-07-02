package com.side.rest.domain.user.dto.request;

import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDiscordAuthRequestDto {

    @Null(groups = Insert.class, message = "Insert 시 id는 null이어야 합니다")
    @NotNull(groups = Update.class, message = "Update 시 id는 필수입니다")
    @Positive(groups = Update.class)
    private Long id;
    @NotNull(message = "사용자 ID는 필수입니다")
    @Positive(message = "사용자 ID는 양수여야 합니다")
    private Long userUniqueId;
    @NotBlank(message = "Discord ID는 필수입니다")
    @Size(max = 20, message = "Discord ID는 20자 이하여야 합니다")
    @Pattern(regexp = "^[0-9]+$", message = "Discord ID는 숫자만 가능합니다")
    private String discordId;
    @NotBlank(message = "Discord 사용자명은 필수입니다")
    @Size(min = 1, max = 32, message = "Discord 사용자명은 1-32자 사이여야 합니다")
    private String discordUsername;
    @Size(max = 4, message = "Discord 태그는 4자 이하여야 합니다")
    @Pattern(regexp = "^[0-9]{4}$", message = "Discord 태그는 4자리 숫자여야 합니다")
    private String discordDiscriminator;
    @Size(max = 32, message = "Discord 글로벌 이름은 32자 이하여야 합니다")
    private String discordGlobalName;
    @Email(message = "올바른 이메일 형식이어야 합니다")
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다")
    private String discordEmail;
    @Size(max = 255, message = "Discord 아바타는 255자 이하여야 합니다")
    private String discordAvatar;
    private Boolean discordVerified;

    public interface Insert extends Default {}

    public interface Update extends Default {}
}