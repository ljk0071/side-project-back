package com.side.infrastructure.jpa.entity;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.side.infrastructure.jpa.common.MetadataEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Builder(toBuilder = true)
@Entity
@Table(name = "user_role")
@DynamicInsert
@DynamicUpdate
@IdClass(UserRoleKey.class)
public class UserRoleEntity {

	@Id
	@Comment("사용자 ID")
	@Column(name = "user_unique_id")
	private long userUniqueId;

	@Id
	@Comment("역할 ID")
	@Column(name = "role_id", length = 30)
	private String roleId;

	@Embedded
	private MetadataEntity metadata;
}
