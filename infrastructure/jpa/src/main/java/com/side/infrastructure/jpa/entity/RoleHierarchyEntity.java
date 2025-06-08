package com.side.infrastructure.jpa.entity;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role_hierarchy")
@IdClass(RoleHierarchyKey.class)
public class RoleHierarchyEntity {

	@Id
	@Comment("하위 역할")
	@Column(name = "lower_role", length = 30)
	private String lowerRole;

	@Id
	@Comment("상위 역할")
	@Column(name = "higher_role", length = 30)
	private String higherRole;
}