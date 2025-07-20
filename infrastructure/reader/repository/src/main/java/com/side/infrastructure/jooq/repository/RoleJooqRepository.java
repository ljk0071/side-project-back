package com.side.infrastructure.jooq.repository;

import com.side.domain.Metadata;
import com.side.domain.model.Role;
import com.side.domain.repository.RoleReader;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.side.infrastructure.jooq.generated.tables.Role.ROLE;

@Repository
@RequiredArgsConstructor
public class RoleJooqRepository implements RoleReader {

    private final DSLContext dsl;

    @Override
    public Role findById(String roleId) {
        return dsl.select(ROLE.ID, ROLE.REVISION, ROLE.CODE, ROLE.NAME,
                          ROLE.CREATED_AT, ROLE.CREATED_BY, ROLE.MODIFIED_AT, ROLE.MODIFIED_BY)
                  .from(ROLE)
                  .where(ROLE.CODE.eq(roleId))
                  .fetchOne(this::mapRecordToRole);
    }

    private Role mapRecordToRole(org.jooq.Record record) {
        return Role.builder()
                   .id(record.get(ROLE.ID))
                   .revision(record.get(ROLE.REVISION))
                   .code(record.get(ROLE.CODE))
                   .name(record.get(ROLE.NAME))
                   .metadata(Metadata.builder()
                                     .createdAt(record.get(ROLE.CREATED_AT))
                                     .createdBy(record.get(ROLE.CREATED_BY))
                                     .modifiedAt(record.get(ROLE.MODIFIED_AT))
                                     .modifiedBy(record.get(ROLE.MODIFIED_BY))
                                     .build())
                   .build();
    }
}