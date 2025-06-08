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
public class UserRoleKey implements Serializable {

	@Serial
	private static final long serialVersionUID = -2403286936966239148L;

	@Comment("사용자 고유 ID")
	private String userUniqueId;

	@Comment("역할 ID")
	private String roleId;
}
