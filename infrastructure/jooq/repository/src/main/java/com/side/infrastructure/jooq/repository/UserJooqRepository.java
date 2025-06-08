package com.side.infrastructure.jooq.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jooq.generated.Tables.*;
import static com.side.infrastructure.jooq.generated.tables.Role.*;
import static com.side.infrastructure.jooq.generated.tables.Role.ROLE;
import static com.side.infrastructure.jooq.generated.tables.User.*;
import static com.side.infrastructure.jooq.generated.tables.User.USER;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.side.domain.enums.UserStatus;
import com.side.domain.model.Role;
import com.side.domain.model.User;
import com.side.domain.repository.UserRepository;
import com.side.domain.repository.UserRepositoryManager;

@Repository
public class UserJooqRepository implements UserRepository {

	private final DSLContext dsl;

	public UserJooqRepository(DSLContext dsl) {
		this.dsl = dsl;
		UserRepositoryManager.addUserRepository(JOOQ, this);
	}

	@Override
	public void create(User user) {

	}

	@Override
	public User findById(long uniqueId) {
		return null;
	}

	@Override
	public User findByUserId(String userId) {
		return dsl.select(USER.UNIQUE_ID, USER.USER_ID, USER.PASSWORD, USER.STATUS, ROLE.ID, ROLE.NAME)
				  .from(USER)
				  .innerJoin(USER_ROLE)
				  .on(USER.UNIQUE_ID.eq(USER_ROLE.USER_UNIQUE_ID))
				  .innerJoin(ROLE)
				  .on(USER_ROLE.ROLE_ID.eq(ROLE.ID))
				  .where(USER.USER_ID.eq(userId))
				  .fetchGroups(record -> User.builder()
											 .uniqueId(record.get(USER.UNIQUE_ID))
											 .userId(record.get(USER.USER_ID))
											 .password(record.get(USER.PASSWORD))
											 .status(record.get(USER.STATUS))
											 .build(),
					  record -> Role.builder().id(record.get(ROLE.ID)).name(record.get(ROLE.NAME)).build())
				  .entrySet()
				  .stream()
				  .map(entry -> entry.getKey().toBuilder().roles(entry.getValue()).build())
				  .findFirst()
				  .orElse(null);
	}

	@Override
	public User findByUserIdAndStatus(String userId, UserStatus status) {
		return null;
	}
}
