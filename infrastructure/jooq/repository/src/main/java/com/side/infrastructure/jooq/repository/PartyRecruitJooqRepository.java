package com.side.infrastructure.jooq.repository;

import com.side.domain.Metadata;
import com.side.domain.Search;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.enums.SearchType;
import com.side.domain.enums.UserStatus;
import com.side.domain.exception.InvalidSearchCondition;
import com.side.domain.model.Article;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitReader;
import com.side.infrastructure.jooq.generated.tables.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.side.infrastructure.jooq.generated.Tables.PARTY_RECRUIT;
import static com.side.infrastructure.jooq.generated.tables.User.USER;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PartyRecruitJooqRepository implements PartyRecruitReader {

    private final DSLContext dsl;

    @Override
    public Optional<PartyRecruit> findByRecruitId(long partyRecruitId) {

        User createUser = USER.as("create_user");
        User modifyUser = USER.as("modify_user");

        return dsl.select(PARTY_RECRUIT.asterisk(), createUser.NAME.as("create_user_name"), modifyUser.NAME.as("modify_user_name"))
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
                           .status(record.get(PARTY_RECRUIT.STATUS))
                           .metadata(Metadata.builder()
                                             .createdBy(record.get(PARTY_RECRUIT.CREATED_BY))
                                             .createdByName(record.get("create_user_name", String.class))
                                             .createdAt(record.get(PARTY_RECRUIT.CREATED_AT))
                                             .modifiedBy(record.get(PARTY_RECRUIT.MODIFIED_BY))
                                             .modifiedByName(record.get("modify_user_name", String.class))
                                             .modifiedAt(record.get(PARTY_RECRUIT.MODIFIED_AT))
                                             .build())
                           .build();
    }

    public List<PartyRecruit> getActiveRecruits(@NonNull Search search) {

        var query = dsl.select(PARTY_RECRUIT.ID, PARTY_RECRUIT.TITLE)
                       .from(PARTY_RECRUIT)
                       .where(PARTY_RECRUIT.STATUS.eq(YesNoDeleteStatus.YES));

        // 검색어가 있는 경우 동적 쿼리 적용
        if (StringUtils.hasText(search.searchKeyword())) {
            Condition searchConditions = buildSearchConditions(search);
            query = query.and(searchConditions);
        }

        return query.orderBy(PARTY_RECRUIT.CREATED_AT.desc())
                    .fetch(record -> PartyRecruit.builder()
                                                 .id(record.get(PARTY_RECRUIT.ID))
                                                 .article(Article.builder()
                                                                 .title(record.get(PARTY_RECRUIT.TITLE))
                                                                 .build())
                                                 .build());
    }

    private Condition buildSearchConditions(Search search) {

        return search.searchConditions()
                     .stream()
                     .map(type -> buildConditionByField(type, search.searchKeyword()))
                     .reduce(Condition::or)
                     .orElseThrow(() -> new InvalidSearchCondition("검색어는 있는데 검색조건이 없습니다."));
    }

    private Condition buildConditionByField(SearchType type, String keyword) {

        return switch (type) {
            case ALL -> PARTY_RECRUIT.TITLE.containsIgnoreCase(keyword)
                                           .or(PARTY_RECRUIT.CONTENTS.containsIgnoreCase(keyword));
            case TITLE -> PARTY_RECRUIT.TITLE.containsIgnoreCase(keyword);
            case CONTENTS -> PARTY_RECRUIT.CONTENTS.containsIgnoreCase(keyword);
        };
    }
}
