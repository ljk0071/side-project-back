package com.side.infrastructure.jooq.repository;

import com.side.domain.Metadata;
import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Article;
import com.side.domain.model.Notice;
import com.side.domain.repository.NoticeRepository;
import com.side.domain.repository.NoticeRepositoryManager;
import com.side.infrastructure.jooq.generated.tables.records.NoticeRecord;
import com.side.infrastructure.jooq.repository.base.AutoAuditJooqRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep6;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.side.infrastructure.jooq.generated.tables.Notice.NOTICE;
import static com.side.infrastructure.jooq.generated.tables.User.USER;

import com.side.infrastructure.jooq.generated.tables.User;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.side.domain.RepositoryTypeEnum.JOOQ;
import static com.side.infrastructure.jooq.generated.tables.Notice.NOTICE;
import static com.side.infrastructure.jooq.generated.tables.User.USER;

@Slf4j
@Repository
public class NoticeJooqRepository extends AutoAuditJooqRepository<NoticeRecord> implements NoticeRepository {

    public NoticeJooqRepository(DSLContext dsl) {
        super(dsl, NoticeRecord.class);
        NoticeRepositoryManager.addNoticeRepository(JOOQ, this);
    }

    @Override
    public void create(Notice notice) {

        NoticeRecord noticeRecord = dsl.newRecord(NOTICE, notice);

        noticeRecord.setTitle(notice.article().title());
        noticeRecord.setContents(notice.article().contents());
        noticeRecord.setViewCount(notice.viewCount());
        noticeRecord.setRevision(notice.revision());

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
                                                                                                                                      NOTICE.VIEW_COUNT,
                                                                                                                                      NOTICE.REVISION,
                                                                                                                                      NOTICE.CREATED_AT,
                                                                                                                                      NOTICE.CREATED_BY)
                                                                                                                              .values(
                                                                                                                                      notice.article()
                                                                                                                                            .title(),
                                                                                                                                      notice.article()
                                                                                                                                            .contents(),
                                                                                                                                      notice.viewCount(),
                                                                                                                                      notice.revision(),
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
    public List<Notice> find(String keyword, NoticeSearchType type) {

        Condition condition;

        if (type == NoticeSearchType.TITLE) {
            condition = NOTICE.TITLE.containsIgnoreCase(keyword);

        } else if (type == NoticeSearchType.CONTENT) {
            condition = NOTICE.CONTENTS.containsIgnoreCase(keyword);

        } else if (type == NoticeSearchType.ALL) {
            condition = NOTICE.TITLE.containsIgnoreCase(keyword)
                                    .or(NOTICE.CONTENTS.containsIgnoreCase(keyword));

        } else {
            condition = DSL.trueCondition();
        }

        return dsl.selectFrom(NOTICE)
                  .where(condition)
                  .orderBy(NOTICE.CREATED_AT.desc())
                  .fetchInto(Notice.class);
    }

    @Override
    public Optional<Notice> findById(Long id) {

        User createUser = USER.as("create_user");
        User modifyUser = USER.as("modify_user");

        return dsl.select(NOTICE.asterisk(),
                          createUser.NAME.as("created_by_name"),
                          modifyUser.NAME.as("modified_by_name")
                  )
                  .from(NOTICE)
                  .where(NOTICE.ID.eq(id))
                  .fetchOptional()
                  .map(record -> Notice.builder()
                                       .id(record.get(NOTICE.ID))
                                       .revision(record.get(NOTICE.REVISION))
                                       .viewCount(record.get(NOTICE.VIEW_COUNT))
                                       .article(Article.builder()
                                                       .title(record.get(NOTICE.TITLE))
                                                       .contents(record.get(NOTICE.CONTENTS))
                                                       .viewCount(record.get(NOTICE.VIEW_COUNT))
                                                       .build())
                                       .metadata(Metadata.builder()
                                                         .createdBy(record.get(NOTICE.CREATED_BY))
                                                         .createdByName(record.get("create_user_name", String.class))
                                                         .createdAt(record.get(NOTICE.CREATED_AT))
                                                         .modifiedBy(record.get(NOTICE.MODIFIED_BY))
                                                         .modifiedByName(record.get("modified_user_name", String.class))
                                                         .modifiedAt(record.get(NOTICE.MODIFIED_AT))
                                                         .build())
                                       .build());
    }


}
