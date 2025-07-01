package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.StatusTypeEnum;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserRole(
	Long id,
	Long revision,
	StatusTypeEnum status,
	Long roleId,
	Long userUniqueId,
	Metadata metadata
) {
}