package com.side.domain.model;

import com.side.domain.Metadata;

import lombok.Builder;

@Builder(toBuilder = true)
public record PartyRecruit(Long id, Article article, Comment comment, Metadata metadata) {
}