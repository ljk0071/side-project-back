package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.StatusTypeEnum;
import lombok.Builder;

@Builder(toBuilder = true)
public record Resume(Long id, Long revision, Long userUniqueId, StatusTypeEnum status, String contents,
                     Metadata metadata) {
}