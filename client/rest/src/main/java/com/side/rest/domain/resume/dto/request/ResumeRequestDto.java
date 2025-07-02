package com.side.rest.domain.resume.dto.request;

import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeRequestDto {

    @Null(groups = Insert.class, message = "Insert 시 id는 null이어야 합니다")
    @NotNull(groups = Update.class, message = "Update 시 id는 필수입니다")
    @Positive(groups = Update.class)
    private Long id;
    @Null(groups = Insert.class, message = "Insert 시 userUniqueId는 null이어야 합니다")
    private Long userUniqueId;
    @NotBlank(groups = {Insert.class, Update.class}, message = "이력서 내용은 필수입니다")
    @Size(min = 1, max = 255, groups = {Insert.class, Update.class}, message = "이력서 내용은 1-255자 사이여야 합니다")
    private String contents;

    public interface Insert extends Default {}

    public interface Update extends Default {}
}