package com.side.infrastructure.jooq.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jooq.generated.tables.Notice.*;
import static com.side.security.service.SecurityHelper.*;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep6;
import org.jooq.RecordListener;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.side.domain.model.Notice;
import com.side.domain.repository.NoticeRepository;
import com.side.domain.repository.NoticeRepositoryManager;
import com.side.infrastructure.jooq.config.RecordAuditListenerGenerator;
import com.side.infrastructure.jooq.generated.tables.records.NoticeRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class NoticeJooqRepository implements NoticeRepository {

	private final DSLContext dsl;

	public NoticeJooqRepository(DSLContext dsl) {
		Configuration config = dsl.configuration().derive();
		config.set(noticeRecordAuditListener());
		this.dsl = DSL.using(config);

		NoticeRepositoryManager.addNoticeRepository(JOOQ, this);
	}

	private RecordListener noticeRecordAuditListener() {
		Consumer<NoticeRecord> createAudit = record -> {
			record.setCreatedAt(Instant.now());
			record.setCreatedBy(getAuthenticatedUser().uniqueId());
		};

		Consumer<NoticeRecord> updateAudit = record -> {
			record.setModifiedAt(Instant.now());
			record.setModifiedBy(2L);
		};

		return new RecordAuditListenerGenerator<NoticeRecord>().generate(
			NoticeRecord.class,
			createAudit,
			updateAudit
		);
	}

	@Override
	public void create(Notice notice) {

		NoticeRecord noticeRecord = dsl.newRecord(NOTICE, notice);

		noticeRecord.setTitle(notice.article().title());
		noticeRecord.setContents(notice.article().contents());
		noticeRecord.setLikes(notice.article().userReaction().likes());
		noticeRecord.setDislikes(notice.article().userReaction().dislikes());

		noticeRecord.insert();
	}

	@Override
	public void bulkcreate(List<Notice> notices) {

		int batchSize = 500; // 또는 1000
		for (int i = 0; i < notices.size(); i += batchSize) {
			int endIndex = Math.min(i + batchSize, notices.size());
			List<Notice> batch = notices.subList(i, endIndex);

			log.info("배치 처리 완료: {} 항목", endIndex);

			// dsl.batch(batch.stream()
			// 			   .map(notice -> dsl.insertInto(NOTICE)
			// 								 .columns(NOTICE.TITLE, NOTICE.CONTENTS, NOTICE.LIKES, NOTICE.DISLIKES,
			// 									 NOTICE.CREATED_AT, NOTICE.CREATED_BY)
			// 								 .values(notice.article().contents(), notice.article().contents(),
			// 									 notice.article()
			// 										   .userReaction()
			// 										   .likes(),
			// 									 notice.article().userReaction().dislikes(),
			// 									 LocalDateTime.ofInstant(notice.metadata().createdAt(),
			// 										 ZoneId.systemDefault()),
			// 									 notice.metadata().createdBy()))
			// 			   .toList())
			//    .execute();

			List<InsertValuesStep6<NoticeRecord, String, String, Long, Long, Instant, Long>> temp = batch.stream()
																										 .map(
																											 notice -> dsl.insertInto(
																															  NOTICE)
																														  .columns(
																															  NOTICE.TITLE,
																															  NOTICE.CONTENTS,
																															  NOTICE.LIKES,
																															  NOTICE.DISLIKES,
																															  NOTICE.CREATED_AT,
																															  NOTICE.CREATED_BY)
																														  .values(
																															  notice.article()
																																	.contents(),
																															  notice.article()
																																	.contents(),
																															  notice.article()
																																	.userReaction()
																																	.likes(),
																															  notice.article()
																																	.userReaction()
																																	.dislikes(),
																															  notice.metadata()
																																	.createdAt(),
																															  notice.metadata()
																																	.createdBy()))
																										 .toList();

			CompletionStage<int[]> future = dsl.batch(temp)
											   .executeAsync();

			recursiveTry(future, temp);

			// 	// 각 배치마다 트랜잭션 처리
			// 	int finalI = i;
			// 	dsl.transaction(configuration -> {
			// 		DSLContext ctx = DSL.using(configuration);
			// 		// 배치 실행 코드
			//
			// 		if (finalI == 2000) {
			// 			throw new RuntimeException();
			// 		}
			// 		ctx.batchInsert(batch.stream()
			// 							 .map(notice -> {
			// 								 NoticeRecord record = ctx.newRecord(NOTICE);
			//
			// 								 record.setTitle(notice.article().title());
			// 								 record.setContents(notice.article().contents());
			// 								 record.setDislikes(notice.article().userReaction().dislikes());
			// 								 record.setLikes(notice.article().userReaction().likes());
			// 								 record.setCreatedAt(LocalDateTime.ofInstant(notice.metadata().createdAt(),
			// 									 ZoneId.systemDefault()));
			// 								 record.setCreatedBy(notice.metadata().createdBy());
			//
			// 								 return record;
			// 							 })
			// 							 .toList())
			// 		   .execute();
			// 	});
		}
		// dsl.batch(notices.stream()
		// 				 .map(notice -> dsl.insertInto(NOTICE)
		// 								   .columns(NOTICE.TITLE, NOTICE.CONTENTS, NOTICE.LIKES, NOTICE.DISLIKES,
		// 									   NOTICE.CREATED_AT, NOTICE.CREATED_BY)
		// 								   .values(notice.article().contents(), notice.article().contents(),
		// 									   notice.article()
		// 											 .userReaction()
		// 											 .likes(),
		// 									   notice.article().userReaction().dislikes(),
		// 									   LocalDateTime.ofInstant(notice.metadata().createdAt(),
		// 										   ZoneId.systemDefault()),
		// 									   notice.metadata().createdBy()))
		// 				 .toList())
		//    .execute();
	}

	private void recursiveTry(CompletionStage<int[]> future,
		List<InsertValuesStep6<NoticeRecord, String, String, Long, Long, Instant, Long>> temp) {
		future.exceptionally(e -> {
			log.error("Batch insert failed - retrying: {}", e.getMessage());
			try {
				CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS)
								 .execute(() -> recursiveTry(dsl.batch(temp)
																.executeAsync(), temp));
			} catch (Exception retryEx) {
				log.error("Retry failed: {}", retryEx.getMessage());
			}
			return null;
		});
	}

	@Override
	public Notice find(Notice notice) {

		return dsl.select(NOTICE.ID, NOTICE.TITLE.as("article.title"))
				  .from(NOTICE)
				  .where(NOTICE.TITLE.eq(String.valueOf(notice.id())))
				  .fetchOneInto(Notice.class);
	}
}
