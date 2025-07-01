package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.StatusTypeEnum;

import lombok.Builder;

@Builder(toBuilder = true)
public record PartyRecruit(
        Long id,
        Long revision,
        Long userUniqueId,
        Article article,
        Integer maxMembers,
        StatusTypeEnum status,
        Metadata metadata
) {
}