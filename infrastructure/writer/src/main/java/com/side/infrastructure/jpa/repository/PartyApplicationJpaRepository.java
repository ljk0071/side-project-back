package com.side.infrastructure.jpa.repository;

import com.side.domain.AsyncUtil;
import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.PartyApplication;
import com.side.domain.repository.PartyApplicationWriter;
import com.side.infrastructure.jpa.entity.PartyApplicationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;

import static com.side.infrastructure.jpa.mapper.PartyApplicationMapper.PartyApplicationMapper;
import static com.side.security.service.SecurityHelper.getAuthenticatedUserUniqueId;

@RequiredArgsConstructor
@Repository
public class PartyApplicationJpaRepository implements PartyApplicationWriter {

    private final PartyApplicationJpaInterface repository;

    private final JdbcClient jdbcClient;

    @Override
    public long create(PartyApplication partyApplication) {

        return repository.save(PartyApplicationMapper.toEntity(partyApplication)).getId();
    }

    @Override
    public void changeStatus(long partyApplicationId, PartyApplicationStatusTypeEnum status) {

        PartyApplicationEntity entity = repository.findById(partyApplicationId)
                                                  .orElseThrow(() -> new NotExistException("존재 하지 않는 파티 지원 상황입니다.", partyApplicationId));

        if (entity.getStatus() == status) {
            return;
        }

        PartyApplicationEntity originalEntity = entity.toBuilder().build();

        entity.changeStatus(status);

        long modifiedUserUniqueId = getAuthenticatedUserUniqueId();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            AsyncUtil.runAsync(() -> logModified(modifiedUserUniqueId, originalEntity), executor);
        }

    }

    private void logModified(long modifiedBy, PartyApplicationEntity entity) {

        jdbcClient.sql("""
                              insert into party_application_modify_log (id, revision, party_recruit_id, resume_id, status, created_at, created_by)
                              values (:id, :revision, :partyRecruitId, :resumeId, :status, :createdAt, :createdBy)
                          """)
                  .param("id", entity.getId())
                  .param("revision", entity.getRevision())
                  .param("partyRecruitId", entity.getPartyRecruitId())
                  .param("resumeId", entity.getResumeId())
                  .param("status", entity.getStatus().getValue())
                  .param("createdAt", Instant.now())
                  .param("createdBy", modifiedBy)
                  .update();
    }
}