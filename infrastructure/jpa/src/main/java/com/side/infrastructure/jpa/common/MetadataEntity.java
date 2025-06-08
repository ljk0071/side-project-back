package com.side.infrastructure.jpa.common;

import java.time.Instant;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Embeddable
@EntityListeners(AuditingEntityListener.class)
public class MetadataEntity {

	@CreatedBy
	@Comment("생성자")
	@Column(name = "created_by", updatable = false)
	private long createdBy;

	@Comment("생성자 이름")
	@Transient
	private String createdByName;

	@CreatedDate
	@Comment("생성일시")
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;

	@LastModifiedBy
	@Comment("수정자")
	@Column(name = "modified_by")
	private Long modifiedBy;

	@Comment("수정자 이름")
	@Transient
	private String modifiedByName;

	@LastModifiedDate
	@Comment("수정일시")
	@Column(name = "modified_at")
	private Instant modifiedAt;

}
