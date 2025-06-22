package com.side.infrastructure.jpa.entity;

import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.side.domain.StatusTypeEnum;
import com.side.infrastructure.jpa.common.MetadataEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "resume")
@EntityListeners(AuditingEntityListener.class)
public class ResumeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("이력서 ID")
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_unique_id")
	@Comment("유저 고유 ID")
	private UserEntity userEntity;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusTypeEnum status;

	@Column(name = "contents", columnDefinition = "TEXT", nullable = false)
	@Comment("이력서 내용")
	private String contents;

	@Embedded
	private MetadataEntity metadata;
}