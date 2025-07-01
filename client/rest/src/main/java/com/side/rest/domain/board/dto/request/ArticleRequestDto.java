package com.side.rest.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequestDto {

    public interface Insert extends Default {}

    public interface Update extends Default {}

    @NotBlank(groups = {Insert.class, Update.class}, message = "제목은 필수입니다")
    @Size(min = 1, max = 100, groups = {Insert.class, Update.class}, message = "제목은 1-100자 사이여야 합니다")
    private String title;

    @NotBlank(groups = {Insert.class, Update.class}, message = "내용은 필수입니다")
    @Size(min = 1, max = 255, groups = {Insert.class, Update.class}, message = "내용은 1-255자 사이여야 합니다")
    private String contents;
}