package com.side.infrastructure.jooq.repository;

import com.side.domain.Metadata;
import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.enums.UserStatus;
import com.side.domain.model.PartyApplication;
import com.side.domain.repository.PartyApplicationReader;
import com.side.infrastructure.jooq.generated.tables.User;
import com.side.infrastructure.jooq.generated.tables.records.PartyApplicationRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.side.infrastructure.jooq.generated.Tables.PARTY_RECRUIT;
import static com.side.infrastructure.jooq.generated.Tables.RESUME;
import static com.side.infrastructure.jooq.generated.tables.PartyApplication.PARTY_APPLICATION;
import static com.side.infrastructure.jooq.generated.tables.User.USER;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PartyApplicationJooqRepository implements PartyApplicationReader {

    private final DSLContext dsl;

    @Override
    public Optional<PartyApplication> findById(long partyApplicationId) {

        User createUser = USER.as("create_user");
        User modifyUser = USER.as("modify_user");

        return dsl.select(
                          PARTY_APPLICATION.asterisk(),
                          createUser.NAME.as("create_user_name"),
                          modifyUser.NAME.as("modify_user_name")
                  )
                  .from(PARTY_APPLICATION)
                  .leftJoin(createUser)
                  .on(PARTY_APPLICATION.CREATED_BY.eq(createUser.UNIQUE_ID))
                  .and(createUser.STATUS.eq(UserStatus.ACTIVE))
                  .leftJoin(modifyUser)
                  .on(PARTY_APPLICATION.MODIFIED_BY.eq(modifyUser.UNIQUE_ID))
                  .and(modifyUser.STATUS.eq(UserStatus.ACTIVE))
                  .where(PARTY_APPLICATION.ID.eq(partyApplicationId))
                  .fetchOptional()
                  .map(this::toDomainWithUserInfo);
    }

    @Override
    public List<PartyApplication> findByPartyRecruitId(long partyRecruitId) {
        return dsl.selectFrom(PARTY_APPLICATION)
                  .where(PARTY_APPLICATION.PARTY_RECRUIT_ID.eq(partyRecruitId))
                  .fetch()
                  .map(this::toDomain);
    }

    @Override
    public List<PartyApplication> findByResumeId(long resumeId) {
        return dsl.selectFrom(PARTY_APPLICATION)
                  .where(PARTY_APPLICATION.RESUME_ID.eq(resumeId))
                  .fetch()
                  .map(this::toDomain);
    }

    @Override
    public boolean existsByPartyRecruitIdAndResumeId(Long partyRecruitId, Long resumeId) {
        return dsl.fetchExists(
                dsl.selectFrom(PARTY_APPLICATION)
                   .where(PARTY_APPLICATION.PARTY_RECRUIT_ID.eq(partyRecruitId))
                   .and(PARTY_APPLICATION.RESUME_ID.eq(resumeId))
        );
    }

    @Override
    public List<Long> findPartyRecruiterCreatorAndResumeCreator(Long partyRecruitId, Long resumeId) {
        return dsl.select(PARTY_RECRUIT.USER_UNIQUE_ID, RESUME.USER_UNIQUE_ID)
                  .from(PARTY_RECRUIT, RESUME)
                  .where(PARTY_RECRUIT.ID.eq(partyRecruitId))
                  .and(RESUME.ID.eq(resumeId))
                  .fetchOne(record -> List.of(
                          record.getValue(PARTY_RECRUIT.USER_UNIQUE_ID),
                          record.getValue(RESUME.USER_UNIQUE_ID)
                  ));
    }

    @Override
    public Optional<PartyApplication> findByRecruitIdAndResumeId(Long partyRecruitId, Long resumeId) {
        return dsl.selectFrom(PARTY_APPLICATION)
                  .where(PARTY_APPLICATION.PARTY_RECRUIT_ID.eq(partyRecruitId))
                  .and(PARTY_APPLICATION.RESUME_ID.eq(resumeId))
                  .fetchOptionalInto(PartyApplication.class);
    }

    @Override
    public Optional<PartyApplication> findByPartyApplicationId(long partyApplicationId) {
        return dsl.selectFrom(PARTY_APPLICATION)
                  .where(PARTY_APPLICATION.ID.eq(partyApplicationId))
                  .fetchOptional()
                  .map(this::toDomain);
    }

    @Override
    public List<PartyApplication> findAll() {
        return dsl.selectFrom(PARTY_APPLICATION)
                  .fetch()
                  .map(this::toDomain);
    }

    @Override
    public List<PartyApplication> findByUserUniqueId(Long userUniqueId) {

        User createUser = USER.as("create_user");
        User modifyUser = USER.as("modify_user");

        return dsl.select(
                          PARTY_APPLICATION.asterisk(),
                          createUser.NAME.as("create_user_name"),
                          modifyUser.NAME.as("modify_user_name")
                  )
                  .from(PARTY_APPLICATION)
                  .join(RESUME).on(PARTY_APPLICATION.RESUME_ID.eq(RESUME.ID))
                  .leftJoin(createUser)
                  .on(PARTY_APPLICATION.CREATED_BY.eq(createUser.UNIQUE_ID))
                  .and(createUser.STATUS.eq(UserStatus.ACTIVE))
                  .leftJoin(modifyUser)
                  .on(PARTY_APPLICATION.MODIFIED_BY.eq(modifyUser.UNIQUE_ID))
                  .and(modifyUser.STATUS.eq(UserStatus.ACTIVE))
                  .where(RESUME.USER_UNIQUE_ID.eq(userUniqueId))
                  .orderBy(PARTY_APPLICATION.CREATED_AT.desc())
                  .fetch()
                  .map(this::toDomainWithUserInfo);
    }

    private PartyApplication toDomain(PartyApplicationRecord record) {
        return PartyApplication.builder()
                               .id(record.getId())
                               .revision(record.getRevision())
                               .partyRecruitId(record.getPartyRecruitId())
                               .resumeId(record.getResumeId())
                               .status(fromDatabase(record.getStatus()))
                               .metadata(Metadata.builder()
                                                 .createdBy(record.getCreatedBy())
                                                 .createdAt(record.getCreatedAt())
                                                 .modifiedBy(record.getModifiedBy())
                                                 .modifiedAt(record.getModifiedAt())
                                                 .build())
                               .build();
    }

    private PartyApplication toDomainWithUserInfo(Record record) {
        return PartyApplication.builder()
                               .id(record.get(PARTY_APPLICATION.ID))
                               .revision(record.get(PARTY_APPLICATION.REVISION))
                               .partyRecruitId(record.get(PARTY_APPLICATION.PARTY_RECRUIT_ID))
                               .resumeId(record.get(PARTY_APPLICATION.RESUME_ID))
                               .status(fromDatabase(record.get(PARTY_APPLICATION.STATUS)))
                               .metadata(Metadata.builder()
                                                 .createdBy(record.get(PARTY_APPLICATION.CREATED_BY))
                                                 .createdAt(record.get(PARTY_APPLICATION.CREATED_AT))
                                                 .modifiedBy(record.get(PARTY_APPLICATION.MODIFIED_BY))
                                                 .modifiedAt(record.get(PARTY_APPLICATION.MODIFIED_AT))
                                                 .createdByName(record.get("create_user_name", String.class))
                                                 .modifiedByName(record.get("modify_user_name", String.class))
                                                 .build())
                               .build();
    }


    private PartyApplicationStatusTypeEnum fromDatabase(String status) {

        if (status == null) {
            throw new IllegalStateException("status can't be null");
        }

        return switch (status) {
            case "P" -> PartyApplicationStatusTypeEnum.PENDING;
            case "A" -> PartyApplicationStatusTypeEnum.ACCEPTED;
            case "R" -> PartyApplicationStatusTypeEnum.REJECTED;
            case "C" -> PartyApplicationStatusTypeEnum.CANCELED;
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        };
    }
}