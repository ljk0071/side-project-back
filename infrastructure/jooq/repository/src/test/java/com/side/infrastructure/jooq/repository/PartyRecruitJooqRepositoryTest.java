package com.side.infrastructure.jooq.repository;

import com.side.bootstrap.SideApplication;
import com.side.domain.Search;
import com.side.domain.enums.SearchType;
import com.side.domain.exception.InvalidSearchCondition;
import com.side.domain.model.PartyRecruit;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = SideApplication.class)
@DisplayName("파티 지원서 jooq 테스트")
@Transactional
class PartyRecruitJooqRepositoryTest {

    @Autowired
    private DSLContext dslContext;

    private PartyRecruitJooqRepository jooqRepository;

    @BeforeEach
    void setUp() {
        jooqRepository = new PartyRecruitJooqRepository(dslContext);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: ALL 검색 조건")
    void getActiveRecruits_Success_AllSearch() {
        // given
        Search search = Search.builder()
                              .searchConditions(List.of(SearchType.ALL))
                              .searchKeyword("파티")
                              .build();

        // when
        List<PartyRecruit> result = jooqRepository.getActiveRecruits(search);

        // then
        assertThat(result).isNotNull();
        // 실제 데이터가 있는 경우 추가 검증 가능
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: TITLE 검색 조건")
    void getActiveRecruits_Success_TitleSearch() {
        // given
        Search search = Search.builder()
                              .searchConditions(List.of(SearchType.TITLE))
                              .searchKeyword("제목")
                              .build();

        // when
        List<PartyRecruit> result = jooqRepository.getActiveRecruits(search);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: CONTENTS 검색 조건")
    void getActiveRecruits_Success_ContentsSearch() {
        // given
        Search search = Search.builder()
                              .searchConditions(List.of(SearchType.CONTENTS))
                              .searchKeyword("내용")
                              .build();

        // when
        List<PartyRecruit> result = jooqRepository.getActiveRecruits(search);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: 여러 검색 조건 (OR 조건)")
    void getActiveRecruits_Success_MultipleConditions() {
        // given
        Search search = Search.builder()
                              .searchConditions(List.of(SearchType.TITLE, SearchType.CONTENTS))
                              .searchKeyword("검색어")
                              .build();

        // when
        List<PartyRecruit> result = jooqRepository.getActiveRecruits(search);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("활성 모집글 조회 - 실패: 검색 조건이 빈 리스트")
    void getActiveRecruits_Fail_EmptySearchConditions() {
        // given
        Search search = Search.builder()
                              .searchConditions(Collections.emptyList())
                              .searchKeyword("검색어")
                              .build();

        // when & then
        assertThatThrownBy(() -> jooqRepository.getActiveRecruits(search))
                .isInstanceOf(InvalidSearchCondition.class)
                .hasMessage("검색어는 있는데 검색조건이 없습니다.");
    }

    @Test
    @DisplayName("활성 모집글 조회 - 실패: 검색 조건이 null")
    void getActiveRecruits_Fail_NullSearchConditions() {
        // given
        Search search = Search.builder()
                              .searchConditions(null)
                              .searchKeyword("검색어")
                              .build();

        // when & then
        assertThatThrownBy(() -> jooqRepository.getActiveRecruits(search))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: 검색어가 없는 경우 (모든 활성 모집글 조회)")
    void getActiveRecruits_Success_NoKeyword() {
        // given
        Search search = Search.builder()
                              .searchConditions(List.of(SearchType.ALL))
                              .searchKeyword("")
                              .build();

        // when
        List<PartyRecruit> result = jooqRepository.getActiveRecruits(search);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: 검색어가 null인 경우")
    void getActiveRecruits_Success_NullKeyword() {
        // given
        Search search = Search.builder()
                              .searchConditions(List.of(SearchType.ALL))
                              .searchKeyword(null)
                              .build();

        // when
        List<PartyRecruit> result = jooqRepository.getActiveRecruits(search);

        // then
        assertThat(result).isNotNull();
    }
}