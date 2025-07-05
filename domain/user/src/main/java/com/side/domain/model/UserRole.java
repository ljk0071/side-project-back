package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserRole(
        Long id,
        Long revision,
        YesNoDeleteStatus status,
        Long roleId,
        Long userUniqueId,
        Metadata metadata
) {
}