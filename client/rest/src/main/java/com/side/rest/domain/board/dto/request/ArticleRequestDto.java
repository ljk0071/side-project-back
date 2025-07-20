package com.side.rest.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequestDto {

    @Size(min = 1, max = 100, message = "제목은 1-100자 사이여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 1, max = 255, message = "내용은 1-255자 사이여야 합니다")
    private String contents;
}