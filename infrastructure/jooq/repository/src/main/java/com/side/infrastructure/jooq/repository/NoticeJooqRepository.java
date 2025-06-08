package com.side.infrastructure.jooq.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jooq.generated.tables.Notice.*;
import static com.side.security.service.SecurityHelper.*;

import java.time.Instant;
import java.util.function.Consumer;

import org.jooq.Configuration;
import org.jooq.DSLContext;
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
	public Notice find(Notice notice) {

		return dsl.select(NOTICE.ID, NOTICE.TITLE.as("article.title"))
				  .from(NOTICE)
				  .where(NOTICE.TITLE.eq(String.valueOf(notice.id())))
				  .fetchOneInto(Notice.class);
	}
}
