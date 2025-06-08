package com.side.infrastructure.jpa.entity;

import java.util.List;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;
import com.side.infrastructure.jpa.common.MetadataEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unique_id")
	private Long uniqueId;

	@Column(name = "user_id", length = 50)
	private String userId;

	@Comment("비밀번호")
	private String password;

	@Comment("이름")
	@Column(length = 50)
	private String name;

	@Comment("핸드폰번호")
	@Column(name = "phone_number", length = 15)
	private String phoneNumber;

	@Comment("이메일")
	private String email;

	@Comment("상태")
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Comment("타입")
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private UserType type;

	@Comment("설명")
	@Column(columnDefinition = "TEXT")
	private String description;

	@Embedded
	private MetadataEntity metadata;

	@Transient
	private List<RoleEntity> roles;
}
