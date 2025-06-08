package com.side.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Comment(Long id, String contents, Long boardId, UserReaction userReaction) {
}