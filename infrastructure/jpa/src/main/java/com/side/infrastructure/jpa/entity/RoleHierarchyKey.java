package com.side.infrastructure.jpa.entity;

import java.io.Serial;
import java.io.Serializable;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class RoleHierarchyKey implements Serializable {

	@Serial
	private static final long serialVersionUID = -5285826462359427736L;

	@Comment("하위 역할")
	private String lowerRole;

	@Comment("상위 역할")
	private String higherRole;
}