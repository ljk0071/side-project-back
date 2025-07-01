package com.side.infrastructure.jooq.repository;

import static com.side.domain.RepositoryTypeEnum.JOOQ;
import static com.side.infrastructure.jooq.generated.Tables.*;
import static com.side.security.service.SecurityHelper.*;

import java.time.Instant;
import java.util.function.Consumer;

import com.side.domain.StatusTypeEnum;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.RecordListener;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitRepository;
import com.side.domain.repository.PartyRecruitRepositoryManager;
import com.side.infrastructure.jooq.config.RecordAuditListenerGenerator;
import com.side.infrastructure.jooq.generated.tables.records.PartyRecruitRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PartyRecruitJooqRepository implements PartyRecruitRepository {

    private final DSLContext dsl;

    public PartyRecruitJooqRepository(DSLContext dsl) {
        Configuration config = dsl.configuration().derive();
        config.set(partyRecruitRecordAuditListener());
        this.dsl = DSL.using(config);

        PartyRecruitRepositoryManager.addPartyRecruitRepository(JOOQ, this);
    }

    private RecordListener partyRecruitRecordAuditListener() {
        Consumer<PartyRecruitRecord> createAudit = record -> {
            record.setCreatedAt(Instant.now());
            record.setCreatedBy(getAuthenticatedUser().uniqueId());
        };

        Consumer<PartyRecruitRecord> updateAudit = record -> {
            record.setModifiedAt(Instant.now());
            record.setModifiedBy(getAuthenticatedUser().uniqueId());
        };

        return new RecordAuditListenerGenerator<PartyRecruitRecord>().generate(
                PartyRecruitRecord.class,
                createAudit,
                updateAudit
        );
    }

    @Override
    public void create(PartyRecruit partyRecruit) {
        PartyRecruitRecord record = dsl.newRecord(PARTY_RECRUIT, partyRecruit);

        record.setUserUniqueId(partyRecruit.userUniqueId());
        record.setTitle(partyRecruit.article().title());
        record.setContents(partyRecruit.article().contents());
        record.setMaxMembers(partyRecruit.maxMembers());
        record.setRevision(partyRecruit.revision());
        record.setStatus(partyRecruit.status().name());

        record.insert();
    }

    @Override
    public void delete(long partyId) {
        dsl.update(PARTY_RECRUIT)
           .set(PARTY_RECRUIT.STATUS, StatusTypeEnum.D.name())
           .where(PARTY_RECRUIT.ID.eq(partyId))
           .execute();
    }
}
