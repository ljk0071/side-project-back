package com.side.infrastructure.jpa.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jpa.mapper.NoticeMapper.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.side.domain.model.Notice;
import com.side.domain.repository.NoticeRepository;
import com.side.domain.repository.NoticeRepositoryManager;
import com.side.infrastructure.jpa.entity.ArticleEntity;
import com.side.infrastructure.jpa.entity.NoticeEntity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

	private final NoticeJpaRepository repository;

	@PostConstruct
	public void init() {
		NoticeRepositoryManager.addNoticeRepository(JPA, this);
	}

	@Override
	public void create(Notice notice) {

		NoticeEntity noticeEntity = NoticeEntity.builder()
												.article(ArticleEntity.builder()
																	  .title(String.valueOf(notice.id()))
																	  .build())
												.build();
		repository.save(noticeEntity);
	}

	@Override
	public void bulkcreate(List<Notice> notices) {

	}

	@Override
	public Notice find(Notice notice) {

		NoticeEntity entity = repository.findByArticle_Title(String.valueOf(notice.id()))
										.orElseThrow(() -> new IllegalStateException(notice.id() + "doesn't exist"));

		return NoticeMapper.toDomain(entity);
	}
}
