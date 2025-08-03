package com.side.infrastructure.jooq.repository;

import com.side.bootstrap.SideApplication;
import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Notice;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.side.domain.YesNoDeleteStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.side.infrastructure.jooq.generated.tables.Notice.NOTICE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SideApplication.class)
@Transactional
@DisplayName("Notice Jooq Repository 테스트")
class NoticeJooqRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    private NoticeJooqRepository jooqRepository;

    @BeforeEach
    void setUp() {
        jooqRepository = new NoticeJooqRepository(dslContext);
    }

    @Test
    @DisplayName("공지사항 단건 조회 - 성공 케이스")
    void findById_Success() {
        // given
        Long testId = 999L;
        String testTitle = "테스트 제목";
        String testContent = "테스트 본문";
        Long viewCount = 123L;
        Long revision = 1L;

        // 테스트용 사용자 ID 및 시간
        Long testUserId = 1L;
        Instant now = Instant.now();

        // insert 테스트 데이터
        dslContext.insertInto(NOTICE)
                  .set(NOTICE.ID, testId)
                  .set(NOTICE.TITLE, testTitle)
                  .set(NOTICE.CONTENTS, testContent)
                  .set(NOTICE.VIEW_COUNT, viewCount)
                  .set(NOTICE.REVISION, revision)
                  .set(NOTICE.STATUS, YesNoDeleteStatus.YES)
                  .set(NOTICE.CREATED_BY, testUserId)
                  .set(NOTICE.CREATED_AT, now)
                  .set(NOTICE.MODIFIED_BY, testUserId)
                  .set(NOTICE.MODIFIED_AT, now)
                  .execute();

        // when
        Optional<Notice> result = jooqRepository.findById(testId);

        // then
        assertThat(result).isPresent();
        Notice notice = result.get();

        assertThat(notice.id()).isEqualTo(testId);
        assertThat(notice.article().title()).isEqualTo(testTitle);
        assertThat(notice.article().contents()).isEqualTo(testContent);
        assertThat(notice.viewCount()).isEqualTo(viewCount);
        assertThat(notice.status()).isEqualTo(YesNoDeleteStatus.YES);
        assertThat(notice.metadata().createdBy()).isEqualTo(testUserId);
    }

    @Test
    @DisplayName("공지사항 단건 조회 - 실패 케이스 (존재하지 않는 ID)")
    void findById_NotFound() {
        // given
        Long nonexistentId = -1L;

        // when
        Optional<Notice> result = jooqRepository.findById(nonexistentId);

        // then
        assertThat(result).isNotPresent(); // Optional.empty() 확인
    }

    @Test
    @DisplayName("공지사항 검색 - 제목 기준 검색")
    void findByTitleKeyword() {
        // given
        dslContext.insertInto(NOTICE)
                  .set(NOTICE.ID, 1L)
                  .set(NOTICE.REVISION, 1L)
                  .set(NOTICE.TITLE, "테스트 제목")
                  .set(NOTICE.CONTENTS, "테스트 본문")
                  .set(NOTICE.STATUS, YesNoDeleteStatus.YES)
                  .set(NOTICE.CREATED_BY, 1L)
                  .set(NOTICE.CREATED_AT, Instant.now())
                  .set(NOTICE.MODIFIED_BY, 1L)
                  .set(NOTICE.MODIFIED_AT, Instant.now())
                  .execute();

        // when
        List<Notice> results = jooqRepository.find("제목", NoticeSearchType.TITLE);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).article().title()).contains("제목");
    }

    @Test
    @DisplayName("공지사항 검색 - 본문 기준 검색")
    void findByContentKeyword() {
        // given
        dslContext.insertInto(NOTICE)
                  .set(NOTICE.ID, 1L)
                  .set(NOTICE.REVISION, 1L)
                  .set(NOTICE.TITLE, "테스트 제목")
                  .set(NOTICE.CONTENTS, "테스트 본문")
                  .set(NOTICE.STATUS, YesNoDeleteStatus.YES)
                  .set(NOTICE.CREATED_BY, 1L)
                  .set(NOTICE.CREATED_AT, Instant.now())
                  .set(NOTICE.MODIFIED_BY, 1L)
                  .set(NOTICE.MODIFIED_AT, Instant.now())
                  .execute();

        // when
        List<Notice> results = jooqRepository.find("본문", NoticeSearchType.CONTENT);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).article().contents()).contains("본문");
    }

    @Test
    @DisplayName("공지사항 검색 - 제목+본문 전체 검색")
    void findByAllKeyword() {
        // given
        dslContext.insertInto(NOTICE)
                  .set(NOTICE.ID, 1L)
                  .set(NOTICE.REVISION, 1L)
                  .set(NOTICE.TITLE, "테스트 제목")
                  .set(NOTICE.CONTENTS, "테스트 본문")
                  .set(NOTICE.STATUS, YesNoDeleteStatus.YES)
                  .set(NOTICE.CREATED_BY, 1L)
                  .set(NOTICE.CREATED_AT, Instant.now())
                  .set(NOTICE.MODIFIED_BY, 1L)
                  .set(NOTICE.MODIFIED_AT, Instant.now())
                  .execute();

        // when
        List<Notice> results = jooqRepository.find("테스트", NoticeSearchType.ALL);

        // then
        assertThat(results).hasSizeGreaterThanOrEqualTo(1);
        assertThat(results.get(0).article().title()).contains("테스트");
    }

    @Test
    @DisplayName("공지사항 검색 - 검색어 없음 또는 미지원 타입")
    void findByUnknownTypeOrEmptyKeyword() {
        // when
        List<Notice> results = jooqRepository.find("아무거나", null); // null 처리 시 trueCondition()

        // then
        assertThat(results).isNotNull(); // 조건 없이 전부 조회되었을 수도 있음
    }
    
}