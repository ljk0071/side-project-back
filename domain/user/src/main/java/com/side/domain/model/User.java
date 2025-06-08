package com.side.domain.model;

import java.util.List;

import com.side.domain.Metadata;
import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;

import lombok.Builder;

@Builder(toBuilder = true)
public record User(
	Long uniqueId,
	String userId,
	String password,
	String name,
	String phoneNumber,
	String email,
	UserStatus status,
	UserType type,
	String description,
	List<Role> roles,
	Metadata metadata
) {
}