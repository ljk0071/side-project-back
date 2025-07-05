package com.side.rest.domain.board.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeRequestDto {

    public interface Insert extends Default {}

    public interface Update extends Default {}

    @Null(groups = Insert.class, message = "Insert 시 id는 null이어야 합니다")
    @NotNull(groups = Update.class, message = "Update 시 id는 필수입니다")
    @Positive(groups = Update.class)
    private Long id;
    
    @Valid
    @NotNull(message = "게시글 정보는 필수입니다")
    private ArticleRequestDto article;
}