package com.side.rest.domain.board.dto.response;

import com.side.rest.domain.user.dto.response.UserReactionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ArticleResponseDto {

    private String title;
    private String contents;
    private long viewCount;
    private UserReactionResponseDto userReaction;
}