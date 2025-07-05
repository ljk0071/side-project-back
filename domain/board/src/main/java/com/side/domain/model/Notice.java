package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import lombok.Builder;

@Builder(toBuilder = true)
public record Notice(
        Long id,
        Long revision,
        Article article,
        Long viewCount,
        YesNoDeleteStatus status,
        Metadata metadata
) {
}