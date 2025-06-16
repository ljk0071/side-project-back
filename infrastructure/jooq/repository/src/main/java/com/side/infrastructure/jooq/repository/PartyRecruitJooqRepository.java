package com.side.infrastructure.jooq.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jooq.generated.Tables.*;
import static com.side.security.service.SecurityHelper.*;

import java.time.Instant;
import java.util.function.Consumer;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.RecordListener;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.side.domain.enums.BoardStatusTypeEnum;
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
		throw new UnsupportedOperationException("jooq에서는 비효율적이므로 soft delete를 하지 않음");
	}

	@Override
	public void delete(long partyId) {
		dsl.update(PARTY_RECRUIT)
		   .set(PARTY_RECRUIT.STATUS, BoardStatusTypeEnum.D.name())
		   .where(PARTY_RECRUIT.ID.eq(partyId))
		   .execute();
	}
}
