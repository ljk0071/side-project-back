package com.side.infrastructure.jooq.repository;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.enums.UserStatus;
import com.side.domain.model.Article;
import com.side.domain.model.PartyApplication;
import com.side.domain.model.PartyRecruit;
import com.side.domain.model.Resume;
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
                          PARTY_RECRUIT.asterisk(),
                          RESUME.asterisk(),
                          createUser.NAME.as("create_user_name"),
                          modifyUser.NAME.as("modify_user_name")
                  )
                  .from(PARTY_APPLICATION)
                  .innerJoin(PARTY_RECRUIT)
                  .on(PARTY_APPLICATION.PARTY_RECRUIT_ID.eq(PARTY_RECRUIT.ID))
                  .and(PARTY_RECRUIT.STATUS.eq(YesNoDeleteStatus.YES))
                  .innerJoin(RESUME)
                  .on(PARTY_APPLICATION.RESUME_ID.eq(RESUME.ID))
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
    public Optional<PartyApplication> findByIdAndResumeId(long partyApplicationId, long resumeId) {

        User createUser = USER.as("create_user");
        User modifyUser = USER.as("modify_user");
        User resumeCreateUser = USER.as("modify_user");

        return dsl.select(
                          PARTY_APPLICATION.asterisk(),
                          PARTY_RECRUIT.asterisk(),
                          RESUME.asterisk(),
                          createUser.NAME.as("create_user_name"),
                          modifyUser.NAME.as("modify_user_name")
                  )
                  .from(PARTY_APPLICATION)
                  .leftJoin(RESUME)
                  .on(PARTY_APPLICATION.RESUME_ID.eq(RESUME.ID))
                  .leftJoin(PARTY_RECRUIT)
                  .on(PARTY_APPLICATION.PARTY_RECRUIT_ID.eq(PARTY_RECRUIT.ID))
                  .and(PARTY_RECRUIT.STATUS.eq(YesNoDeleteStatus.YES))
                  .leftJoin(createUser)
                  .on(PARTY_APPLICATION.CREATED_BY.eq(createUser.UNIQUE_ID))
                  .and(createUser.STATUS.eq(UserStatus.ACTIVE))
                  .leftJoin(modifyUser)
                  .on(PARTY_APPLICATION.MODIFIED_BY.eq(modifyUser.UNIQUE_ID))
                  .and(modifyUser.STATUS.eq(UserStatus.ACTIVE))
                  .where(PARTY_APPLICATION.ID.eq(partyApplicationId))
                  .and(PARTY_APPLICATION.RESUME_ID.eq(resumeId))
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
                          PARTY_RECRUIT.asterisk(),
                          RESUME.asterisk(),
                          createUser.NAME.as("create_user_name"),
                          modifyUser.NAME.as("modify_user_name")
                  )
                  .from(PARTY_APPLICATION)
                  .innerJoin(PARTY_RECRUIT)
                  .on(PARTY_APPLICATION.PARTY_RECRUIT_ID.eq(PARTY_RECRUIT.ID))
                  .and(PARTY_RECRUIT.STATUS.eq(YesNoDeleteStatus.YES))
                  .innerJoin(RESUME)
                  .on(PARTY_APPLICATION.RESUME_ID.eq(RESUME.ID))
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

    @Override
    public List<PartyApplication> findResumes(long userUniqueId) {
        return dsl.select(PARTY_APPLICATION.asterisk(),
                          RESUME.asterisk())
                  .from(PARTY_APPLICATION)
                  .innerJoin(PARTY_RECRUIT)
                  .on(PARTY_APPLICATION.PARTY_RECRUIT_ID.eq(PARTY_RECRUIT.ID))
                  .innerJoin(RESUME)
                  .on(PARTY_APPLICATION.RESUME_ID.eq(RESUME.ID))
                  .where(PARTY_RECRUIT.USER_UNIQUE_ID.eq(userUniqueId))
                  .and(PARTY_RECRUIT.STATUS.eq(YesNoDeleteStatus.YES))
                  .fetch()
                  .map(this::toDomainWithUserInfo);
    }

    @Override
    public List<Long> getOtherApplications(long partyApplicationId, long applicationUniqueId) {

        return dsl.select(PARTY_APPLICATION.ID)
                  .from(PARTY_APPLICATION)
                  .join(RESUME).on(PARTY_APPLICATION.RESUME_ID.eq(RESUME.ID))
                  .where(RESUME.USER_UNIQUE_ID.eq(applicationUniqueId))
                  .and(PARTY_APPLICATION.ID.ne(partyApplicationId))
                  .and(PARTY_APPLICATION.STATUS.ne(PartyApplicationStatusTypeEnum.CANCELED.getValue()))
                  .fetchInto(Long.class);
    }

    private PartyApplication toDomain(PartyApplicationRecord record) {
        return PartyApplication.builder()
                               .id(record.getId())
                               .revision(record.getRevision())
                               .partyRecruit(PartyRecruit.builder()
                                                         .build())
                               .resume(Resume.builder().build())
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
                               .partyRecruit(mapToPartyRecruit(record))
                               .resume(mapToResume(record))
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

    private PartyRecruit mapToPartyRecruit(Record record) {
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
                                             .createdAt(record.get(PARTY_RECRUIT.CREATED_AT))
                                             .modifiedBy(record.get(PARTY_RECRUIT.MODIFIED_BY))
                                             .modifiedAt(record.get(PARTY_RECRUIT.MODIFIED_AT))
                                             .build())
                           .build();
    }

    private Resume mapToResume(Record record) {
        return Resume.builder()
                     .id(record.get(RESUME.ID))
                     .revision(record.get(RESUME.REVISION))
                     .userUniqueId(record.get(RESUME.USER_UNIQUE_ID))
                     .status(record.get(RESUME.STATUS))
                     .contents(record.get(RESUME.CONTENTS))
                     .metadata(Metadata.builder()
                                       .createdBy(record.get(RESUME.CREATED_BY))
                                       .createdAt(record.get(RESUME.CREATED_AT))
                                       .modifiedBy(record.get(RESUME.MODIFIED_BY))
                                       .modifiedAt(record.get(RESUME.MODIFIED_AT))
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