package com.side.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Article(String title, String contents, Long viewCount) {
}