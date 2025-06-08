package com.side.infrastructure.jpa.entity;

import java.util.List;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.side.infrastructure.jpa.common.MetadataEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "role")
@DynamicInsert
@DynamicUpdate
public class RoleEntity {

	@Id
	@Comment("역할 ID")
	@Column(length = 30)
	private String id;

	@Comment("역할명")
	@Column(length = 30)
	private String name;

	@Comment("설명")
	private String description;

	@Embedded
	private MetadataEntity metadata;

	@Transient
	private List<UserRoleEntity> userRoles;
}
