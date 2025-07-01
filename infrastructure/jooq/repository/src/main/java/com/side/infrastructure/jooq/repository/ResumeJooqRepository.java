package com.side.infrastructure.jooq.repository;

import com.side.domain.StatusTypeEnum;
import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeRepository;
import com.side.domain.repository.ResumeRepositoryManager;
import com.side.infrastructure.jooq.config.RecordAuditListenerGenerator;
import com.side.infrastructure.jooq.generated.tables.records.ResumeRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.RecordListener;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.function.Consumer;

import static com.side.domain.RepositoryTypeEnum.JOOQ;
import static com.side.infrastructure.jooq.generated.tables.Resume.RESUME;
import static com.side.security.service.SecurityHelper.getAuthenticatedUser;

@Slf4j
@Repository
public class ResumeJooqRepository implements ResumeRepository {

    private final DSLContext dsl;

    public ResumeJooqRepository(DSLContext dsl) {
        Configuration config = dsl.configuration().derive();
        config.set(resumeRecordAuditListener());
        this.dsl = DSL.using(config);

        ResumeRepositoryManager.addResumeRepository(JOOQ, this);
    }

    private RecordListener resumeRecordAuditListener() {
        // JOOQ 코드 생성 후 ResumeRecord 클래스를 사용할 수 있습니다
        Consumer<ResumeRecord> createAudit = record -> {
            record.setCreatedAt(Instant.now());
            record.setCreatedBy(getAuthenticatedUser().uniqueId());
        };

        Consumer<ResumeRecord> updateAudit = record -> {
            record.setModifiedAt(Instant.now());
            record.setModifiedBy(getAuthenticatedUser().uniqueId());
        };

        return new RecordAuditListenerGenerator<ResumeRecord>().generate(
                ResumeRecord.class,
                createAudit,
                updateAudit
        );
    }

    @Override
    public void create(Resume resume) {
        throw new UnsupportedOperationException("jooq에서는 비효율적이므로 insert를 하지 않음");
    }

    @Override
    public void delete(long resumeId) {
        dsl.update(RESUME)
           .set(RESUME.STATUS, StatusTypeEnum.D.name())
           .where(RESUME.ID.eq(resumeId))
           .execute();
    }
}