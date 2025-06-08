package com.side.infrastructure.jooq.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jooq.generated.tables.Role.*;
import static com.side.infrastructure.jooq.generated.tables.User.*;

import java.util.Collection;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.side.domain.model.Role;
import com.side.domain.repository.UserRoleRepository;
import com.side.domain.repository.UserRoleRepositoryManager;
import com.side.infrastructure.jooq.generated.tables.UserRole;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRoleJooqRepository implements UserRoleRepository {

	private final DSLContext dsl;

	@PostConstruct
	public void init() {
		UserRoleRepositoryManager.addUserRoleRepository(JOOQ, this);
	}

	@Override
	public Collection<Role> loadRoleByUserId(String userId) {

		return dsl.select(ROLE.ID, ROLE.NAME, ROLE.DESCRIPTION)
				  .from(UserRole.USER_ROLE)
				  .innerJoin(ROLE)
				  .on(UserRole.USER_ROLE.ROLE_ID.eq(ROLE.ID))
				  .innerJoin(USER)
				  .on(USER.UNIQUE_ID.eq(UserRole.USER_ROLE.USER_UNIQUE_ID))
				  .where(USER.USER_ID.eq(userId))
				  .fetch()
				  .map(record -> Role.builder()
									 .id(record.get(ROLE.ID))
									 .name(record.get(ROLE.NAME))
									 .description(record.get(ROLE.DESCRIPTION))
									 .build());
	}
}