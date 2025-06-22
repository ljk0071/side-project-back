package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.enums.BoardStatusTypeEnum;

import lombok.Builder;

@Builder(toBuilder = true)
public record PartyRecruit(Long id, BoardStatusTypeEnum status, Article article, Comment comment, Metadata metadata) {
}