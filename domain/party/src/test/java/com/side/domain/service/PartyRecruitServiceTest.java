package com.side.domain.service;

import com.side.domain.Search;
import com.side.domain.enums.SearchType;
import com.side.domain.exception.InvalidSearchCondition;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitReader;
import com.side.domain.repository.PartyRecruitWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class PartyRecruitServiceTest {

    @Mock
    private PartyRecruitReader partyRecruitReader;

    @Mock
    private PartyRecruitWriter partyRecruitWriter;

    @InjectMocks
    private PartyRecruitService partyRecruitService;

    private Search validSearch;
    private List<PartyRecruit> mockPartyRecruits;

    @BeforeEach
    void setUp() {
        validSearch = Search.builder()
                            .searchConditions(List.of(SearchType.ALL))
                            .searchKeyword("파티")
                            .build();

        mockPartyRecruits = List.of(
                PartyRecruit.builder().id(1L).build(),
                PartyRecruit.builder().id(2L).build()
        );
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공")
    void getActiveRecruits_Success() {
        // given
        given(partyRecruitReader.getActiveRecruits(validSearch))
                .willReturn(mockPartyRecruits);

        // when
        List<PartyRecruit> result = partyRecruitService.getActiveRecruits(validSearch);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(mockPartyRecruits);
        verify(partyRecruitReader).getActiveRecruits(validSearch);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: 빈 결과")
    void getActiveRecruits_Success_EmptyResult() {
        // given
        given(partyRecruitReader.getActiveRecruits(validSearch))
                .willReturn(Collections.emptyList());

        // when
        List<PartyRecruit> result = partyRecruitService.getActiveRecruits(validSearch);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(partyRecruitReader).getActiveRecruits(validSearch);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: TITLE 검색 조건")
    void getActiveRecruits_Success_TitleSearch() {
        // given
        Search titleSearch = Search.builder()
                                   .searchConditions(List.of(SearchType.TITLE))
                                   .searchKeyword("제목")
                                   .build();

        given(partyRecruitReader.getActiveRecruits(titleSearch))
                .willReturn(mockPartyRecruits);

        // when
        List<PartyRecruit> result = partyRecruitService.getActiveRecruits(titleSearch);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(partyRecruitReader).getActiveRecruits(titleSearch);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: 여러 검색 조건")
    void getActiveRecruits_Success_MultipleConditions() {
        // given
        Search multiSearch = Search.builder()
                                   .searchConditions(List.of(SearchType.TITLE, SearchType.CONTENTS))
                                   .searchKeyword("검색어")
                                   .build();

        given(partyRecruitReader.getActiveRecruits(multiSearch))
                .willReturn(mockPartyRecruits);

        // when
        List<PartyRecruit> result = partyRecruitService.getActiveRecruits(multiSearch);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(partyRecruitReader).getActiveRecruits(multiSearch);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 실패: 검색 조건이 null")
    void getActiveRecruits_Fail_NullSearchConditions() {
        // given
        Search invalidSearch = Search.builder()
                                     .searchConditions(null)
                                     .searchKeyword("검색어")
                                     .build();

        // when & then
        assertThatThrownBy(() -> partyRecruitService.getActiveRecruits(invalidSearch))
                .isInstanceOf(InvalidSearchCondition.class)
                .hasMessage("검색조건이 존재하지 않습니다. 검색 조건은 하나 이상이어야 합니다.");

        verifyNoInteractions(partyRecruitReader);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 실패: 검색 조건이 빈 리스트")
    void getActiveRecruits_Fail_EmptySearchConditions() {
        // given
        Search invalidSearch = Search.builder()
                                     .searchConditions(Collections.emptyList())
                                     .searchKeyword("검색어")
                                     .build();

        // when & then
        assertThatThrownBy(() -> partyRecruitService.getActiveRecruits(invalidSearch))
                .isInstanceOf(InvalidSearchCondition.class)
                .hasMessage("검색조건이 존재하지 않습니다. 검색 조건은 하나 이상이어야 합니다.");

        verifyNoInteractions(partyRecruitReader);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: 검색어가 null인 경우 (모든 활성 모집글)")
    void getActiveRecruits_Success_NullKeyword() {
        // given
        Search searchWithNullKeyword = Search.builder()
                                             .searchConditions(List.of(SearchType.ALL))
                                             .searchKeyword(null)
                                             .build();

        given(partyRecruitReader.getActiveRecruits(searchWithNullKeyword))
                .willReturn(mockPartyRecruits);

        // when
        List<PartyRecruit> result = partyRecruitService.getActiveRecruits(searchWithNullKeyword);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(partyRecruitReader).getActiveRecruits(searchWithNullKeyword);
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: 검색어가 빈 문자열인 경우")
    void getActiveRecruits_Success_EmptyKeyword() {
        // given
        Search searchWithEmptyKeyword = Search.builder()
                                              .searchConditions(List.of(SearchType.ALL))
                                              .searchKeyword("")
                                              .build();

        given(partyRecruitReader.getActiveRecruits(searchWithEmptyKeyword))
                .willReturn(mockPartyRecruits);

        // when
        List<PartyRecruit> result = partyRecruitService.getActiveRecruits(searchWithEmptyKeyword);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(partyRecruitReader).getActiveRecruits(searchWithEmptyKeyword);
    }
}