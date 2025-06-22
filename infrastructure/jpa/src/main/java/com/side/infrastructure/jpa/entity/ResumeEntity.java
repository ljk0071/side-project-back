package com.side.infrastructure.jpa.entity;

import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.side.domain.enums.BoardStatusTypeEnum;
import com.side.infrastructure.jpa.common.MetadataEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private BoardStatusTypeEnum status;

	@Column(name = "contents", columnDefinition = "TEXT", nullable = false)
	@Comment("이력서 내용")
	private String contents;

	@Embedded
	private MetadataEntity metadata;
}