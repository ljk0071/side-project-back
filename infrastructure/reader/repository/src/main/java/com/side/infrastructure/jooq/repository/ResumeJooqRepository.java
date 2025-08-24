package com.side.infrastructure.jooq.repository;

import com.side.domain.YesNoDeleteStatus;
import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeReader;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.side.infrastructure.jooq.generated.Tables.PARTY_APPLICATION;
import static com.side.infrastructure.jooq.generated.tables.Resume.RESUME;


@RequiredArgsConstructor
@Repository
public class ResumeJooqRepository implements ResumeReader {

    private final DSLContext dslContext;

    @Override
    public Optional<Resume> findByUserUniqueId(Long userUniqueId) {

        return dslContext.selectFrom(RESUME)
                         .where(RESUME.USER_UNIQUE_ID.eq(userUniqueId))
                         .and(RESUME.STATUS.eq(YesNoDeleteStatus.YES))
                         .orderBy(RESUME.CREATED_AT.desc())
                         .limit(1)
                         .fetchOptional()
                         .map(record -> Resume.builder()
                                              .id(record.getId())
                                              .revision(record.getRevision())
                                              .userUniqueId(record.getUserUniqueId())
                                              .status(record.getStatus())
                                              .contents(record.getContents())
                                              .build());
    }

    @Override
    public Optional<Resume> findByResumeId(long resumeId) {
        return dslContext.selectFrom(RESUME)
                         .where(RESUME.ID.eq(resumeId))
                         .and(RESUME.STATUS.eq(YesNoDeleteStatus.YES))
                         .orderBy(RESUME.CREATED_AT.desc())
                         .fetchOptional()
                         .map(record -> Resume.builder()
                                              .id(record.getId())
                                              .revision(record.getRevision())
                                              .userUniqueId(record.getUserUniqueId())
                                              .status(record.getStatus())
                                              .contents(record.getContents())
                                              .build());
    }

    @Override
    public Optional<Resume> findByApplicationId(long partyApplicationId) {
        return dslContext.select(RESUME.asterisk())
                         .from(PARTY_APPLICATION)
                         .innerJoin(RESUME)
                         .on(RESUME.ID.eq(PARTY_APPLICATION.RESUME_ID))
                         .where(PARTY_APPLICATION.ID.eq(partyApplicationId))
                         .fetchOptional()
                         .map(record -> Resume.builder()
                                              .id(record.get(RESUME.ID))
                                              .revision(record.get(RESUME.REVISION))
                                              .userUniqueId(record.get(RESUME.USER_UNIQUE_ID))
                                              .status(record.get(RESUME.STATUS))
                                              .contents(record.get(RESUME.CONTENTS))
                                              .build());
    }
}