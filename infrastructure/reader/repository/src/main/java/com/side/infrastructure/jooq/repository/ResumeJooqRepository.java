package com.side.infrastructure.jooq.repository;

import com.side.domain.YesNoDeleteStatus;
import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeReader;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}