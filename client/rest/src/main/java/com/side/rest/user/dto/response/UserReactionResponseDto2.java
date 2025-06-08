package com.side.rest.user.dto.response;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserReactionResponseDto2(long likes, long dislikes) {
}