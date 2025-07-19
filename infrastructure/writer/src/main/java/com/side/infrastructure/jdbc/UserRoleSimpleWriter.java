package com.side.infrastructure.jdbc;

import com.side.domain.YesNoDeleteStatus;
import com.side.domain.repository.UserRoleWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@RequiredArgsConstructor
@Repository
public class UserRoleSimpleWriter implements UserRoleWriter {

    private final JdbcClient jdbcClient;

    @Override
    public long createNormalUser(long userUniqueId) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                          insert into user_role (revision, user_unique_id, role_id, status, created_at, created_by)
                          values (:revision, :userUniqueId, :roleId, :status, :createdAt, :createdBy)
                          """)
                  .param("revision", 0)
                  .param("userUniqueId", userUniqueId)
                  .param("roleId", 2L)
                  .param("status", YesNoDeleteStatus.YES.getValue())
                  .param("createdAt", Instant.now())
                  .param("createdBy", 0)
                  .update(keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException("유전 권한 정보 저장 후 생성된 키를 가져오는데 실패했습니다.");
        }

        return key.longValue();
    }
}
