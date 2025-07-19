package com.side.infrastructure.jooq.repository;

import com.side.domain.enums.UserStatus;
import com.side.domain.model.Role;
import com.side.domain.model.User;
import com.side.domain.repository.UserReader;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

import static com.side.infrastructure.jooq.generated.Tables.USER_ROLE;
import static com.side.infrastructure.jooq.generated.tables.Role.ROLE;
import static com.side.infrastructure.jooq.generated.tables.User.USER;
import static com.side.infrastructure.jooq.generated.tables.UserDiscordAuth.USER_DISCORD_AUTH;

@RequiredArgsConstructor
@Repository
public class UserJooqRepository implements UserReader {

    private final DSLContext dsl;

    @Override
    public User findById(long uniqueId) {
        return null;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return dsl.select(USER.UNIQUE_ID, USER.USER_ID, USER.NAME, USER.PASSWORD, USER.STATUS, ROLE.ID, ROLE.CODE, ROLE.NAME)
                  .from(USER)
                  .innerJoin(USER_ROLE)
                  .on(USER.UNIQUE_ID.eq(USER_ROLE.USER_UNIQUE_ID))
                  .innerJoin(ROLE)
                  .on(USER_ROLE.ROLE_ID.eq(ROLE.ID))
                  .where(USER.USER_ID.eq(userId))
                  .fetchGroups(this::mapRecordToUser, this::mapRecordToRole)
                  .entrySet()
                  .stream()
                  .map(this::mapEntryToUserWithRoles)
                  .findFirst();
    }

    @Override
    public Optional<User> findByDiscordId(String discordId) {
        return dsl.select(
                          USER.UNIQUE_ID,
                          USER.USER_ID,
                          USER.NAME,
                          USER.PASSWORD,
                          USER.STATUS,
                          ROLE.ID,
                          ROLE.CODE,
                          ROLE.NAME
                  )
                  .from(USER_DISCORD_AUTH)
                  .innerJoin(USER)
                  .on(USER_DISCORD_AUTH.USER_UNIQUE_ID.eq(USER.UNIQUE_ID))
                  .innerJoin(USER_ROLE)
                  .on(USER.UNIQUE_ID.eq(USER_ROLE.USER_UNIQUE_ID))
                  .innerJoin(ROLE)
                  .on(USER_ROLE.ROLE_ID.eq(ROLE.ID))
                  .where(USER_DISCORD_AUTH.DISCORD_ID.eq(discordId))
                  .fetchGroups(this::mapRecordToUser, this::mapRecordToRole)
                  .entrySet()
                  .stream()
                  .map(this::mapEntryToUserWithRoles)
                  .findFirst();
    }

    @Override
    public User findByUserIdAndStatus(String userId, UserStatus status) {
        return null;
    }

    private User mapRecordToUser(Record record) {
        return User.builder()
                   .uniqueId(record.get(USER.UNIQUE_ID))
                   .userId(record.get(USER.USER_ID))
                   .name(record.get(USER.NAME))
                   .password(record.get(USER.PASSWORD))
                   .status(record.get(USER.STATUS))
                   .build();
    }

    private Role mapRecordToRole(Record record) {
        return Role.builder()
                   .id(record.get(ROLE.ID))
                   .code(record.get(ROLE.CODE))
                   .name(record.get(ROLE.NAME))
                   .build();
    }

    private User mapEntryToUserWithRoles(Map.Entry<User, java.util.List<Role>> entry) {
        return entry.getKey().toBuilder()
                    .roles(entry.getValue())
                    .build();
    }
}
