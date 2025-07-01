package com.side.domain.model;

import com.side.domain.Metadata;

import lombok.Builder;

@Builder(toBuilder = true)
public record Role(
        Long id,
        Long revision,
        String code,
        String name,
        Metadata metadata
) {
}
