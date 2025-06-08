package com.side.domain.model;

import com.side.domain.Metadata;

import lombok.Builder;

@Builder(toBuilder = true)
public record Role(
	String id,
	String name,
	String description,
	Metadata metadata
) {
}
