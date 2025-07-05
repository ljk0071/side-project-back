package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import lombok.Builder;

@Builder(toBuilder = true)
public record Resume(Long id, Long revision, Long userUniqueId, YesNoDeleteStatus status, String contents,
                     Metadata metadata) {
}