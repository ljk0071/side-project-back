package com.side.domain.model;

import com.side.domain.Metadata;

import lombok.Builder;

@Builder(toBuilder = true)
public record Resume(Long id, String contents, Metadata metadata) {
}