package com.side.domain.service;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.side.domain.Metadata;
import com.side.domain.model.Article;
import com.side.domain.model.Notice;
import com.side.domain.model.UserReaction;
import com.side.domain.repository.NoticeRepositoryManager;

@Service
public class NoticeService {

	public void create(Notice notice) {
		NoticeRepositoryManager.getNoticeRepository(JOOQ)
							   .create(notice.toBuilder()
											 .article(notice.article().toBuilder()
															.userReaction(UserReaction.initForInsert())
															.build())
											 .build());
	}

	public void bulkCreate(List<Notice> notices) {

		for (int i = 0; i < 10_000; i++) {
			notices.add(Notice.builder()
							  .article(Article.builder()
											  .title(String.valueOf(i))
											  .contents("asd" + i)
											  .build())
							  .build());
		}

		NoticeRepositoryManager.getNoticeRepository(JOOQ)
							   .bulkcreate(notices.stream()
												  .map(notice -> notice.toBuilder()
																	   .article(notice.article().toBuilder()
																					  .userReaction(
																						  UserReaction
																							  .builder()
																							  .likes(0)
																							  .dislikes(0)
																							  .build())
																					  .build())
																	   .metadata(Metadata.builder()
																						 .createdBy(1)
																						 .createdAt(Instant.now())
																						 .build())
																	   .build())
												  .toList());

	}

	public Notice find(Notice notice) {
		return NoticeRepositoryManager.getNoticeRepository(JPA)
									  .find(notice);
	}
}
