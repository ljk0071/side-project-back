package com.side.infrastructure.jooq.repository;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.enums.UserStatus;
import com.side.domain.model.Article;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitReader;
import com.side.infrastructure.jooq.generated.tables.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.side.infrastructure.jooq.generated.Tables.PARTY_RECRUIT;
import static com.side.infrastructure.jooq.generated.tables.User.USER;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PartyRecruitJooqRepository implements PartyRecruitReader {

    private final DSLContext dsl;

    @Override
    public Optional<PartyRecruit> findById(long partyRecruitId) {

        User createUser = USER.as("create_user");
        User modifyUser = USER.as("modify_user");

        return dsl.select(
                          PARTY_RECRUIT.asterisk(),
                          createUser.NAME.as("create_user_name"),
                          modifyUser.NAME.as("modify_user_name")
                  )
                  .from(PARTY_RECRUIT)
                  .leftJoin(createUser)
                  .on(PARTY_RECRUIT.CREATED_BY.eq(createUser.UNIQUE_ID))
                  .and(createUser.STATUS.eq(UserStatus.ACTIVE))
                  .leftJoin(modifyUser)
                  .on(PARTY_RECRUIT.MODIFIED_BY.eq(modifyUser.UNIQUE_ID))
                  .and(modifyUser.STATUS.eq(UserStatus.ACTIVE))
                  .where(PARTY_RECRUIT.ID.eq(partyRecruitId))
                  .fetchOptional(this::toDomainWithUserInfo);
    }

    private PartyRecruit toDomainWithUserInfo(Record record) {
        return PartyRecruit.builder()
                           .id(record.get(PARTY_RECRUIT.ID))
                           .revision(record.get(PARTY_RECRUIT.REVISION))
                           .userUniqueId(record.get(PARTY_RECRUIT.USER_UNIQUE_ID))
                           .article(Article.builder()
                                           .title(record.get(PARTY_RECRUIT.TITLE))
                                           .contents(record.get(PARTY_RECRUIT.CONTENTS))
                                           .build())
                           .maxMembers(record.get(PARTY_RECRUIT.MAX_MEMBERS))
                           .status(fromDatabase(record.get(PARTY_RECRUIT.STATUS)))
                           .metadata(Metadata.builder()
                                             .createdBy(record.get(PARTY_RECRUIT.CREATED_BY))
                                             .createdAt(record.get(PARTY_RECRUIT.CREATED_AT))
                                             .modifiedBy(record.get(PARTY_RECRUIT.MODIFIED_BY))
                                             .modifiedAt(record.get(PARTY_RECRUIT.MODIFIED_AT))
                                             .createdByName(record.get("create_user_name", String.class))
                                             .modifiedByName(record.get("modify_user_name", String.class))
                                             .build())
                           .build();
    }

    private YesNoDeleteStatus fromDatabase(String status) {
        if (status == null) return null;
        return switch (status) {
            case "Y" -> YesNoDeleteStatus.YES;
            case "N" -> YesNoDeleteStatus.NO;
            case "D" -> YesNoDeleteStatus.DELETE;
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        };
    }
}
