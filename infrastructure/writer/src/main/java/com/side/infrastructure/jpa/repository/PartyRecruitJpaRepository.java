package com.side.infrastructure.jpa.repository;

import com.side.domain.AsyncUtil;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitWriter;
import com.side.infrastructure.jpa.entity.PartyApplicationEntity;
import com.side.infrastructure.jpa.entity.PartyRecruitEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;

import static com.side.infrastructure.jpa.mapper.PartyRecruitMapper.PartyRecruitMapper;
import static com.side.security.service.SecurityHelper.getAuthenticatedUserUniqueId;

@RequiredArgsConstructor
@Repository
public class PartyRecruitJpaRepository implements PartyRecruitWriter {

    private final PartyRecruitJpaInterface repository;

    private final JdbcClient jdbcClient;

    @Override
    public long create(PartyRecruit partyRecruit) {

        return repository.save(PartyRecruitMapper.toEntity(partyRecruit)).getId();
    }

    @Override
    public int deleteRecruit(long partyRecruitId) {
        PartyRecruitEntity entity = repository.findById(partyRecruitId)
                                              .orElseThrow(() -> new NotExistException("존재하지 않는 파티 모집글 입니다.", partyRecruitId));

        var originalEntity = entity.toBuilder().build();

        entity.delete();

        long modifiedUserUniqueId = getAuthenticatedUserUniqueId();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            AsyncUtil.runAsync(() -> logModified(modifiedUserUniqueId, originalEntity), executor);
        }

        return 1;
    }

    private void logModified(long modifiedBy, PartyRecruitEntity entity) {


        jdbcClient.sql("""
                              insert into party_recruit_modify_log (id, revision, user_unique_id, contents, max_members, status,  created_at, created_by)
                              values (:id, :revision, :userUniqueId, :contents, :maxMembers, :status, :createdAt, :createdBy)
                          """)
                  .param("id", entity.getId())
                  .param("revision", entity.getRevision())
                  .param("userUniqueId", entity.getUserUniqueId())
                  .param("contents", entity.getArticle().getContents())
                  .param("maxMembers", entity.getMaxMembers())
                  .param("status", entity.getStatus().getValue())
                  .param("createdAt", LocalDateTime.now())
                  .param("createdBy", modifiedBy)
                  .update();
    }
}