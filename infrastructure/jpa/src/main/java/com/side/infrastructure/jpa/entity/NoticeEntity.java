package com.side.infrastructure.jpa.entity;

import org.hibernate.annotations.Comment;

import com.side.infrastructure.jpa.common.MetadataEntity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
@Table(name = "notice")
public class NoticeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("공지사항 ID")
	private Long id;

	@Embedded
	private ArticleEntity article;

	@Transient
	private CommentEntity comment;

	@Embedded
	private MetadataEntity metadata;
}
