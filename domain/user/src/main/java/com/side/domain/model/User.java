package com.side.domain.model;

import java.time.Instant;
import java.util.List;

import com.side.domain.Metadata;
import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;

import lombok.Builder;

@Builder(toBuilder = true)
public record User(
	Long uniqueId,
	Long revision,
	String userId,
	String password,
	Instant passwordUpdatedAt,
	String name,
	String email,
	UserStatus status,
	UserType type,
	String description,
	List<Role> roles,
	Metadata metadata
) {
}