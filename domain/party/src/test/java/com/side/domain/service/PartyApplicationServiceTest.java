package com.side.domain.service;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.model.PartyApplication;
import com.side.domain.repository.PartyApplicationReader;
import com.side.domain.repository.PartyApplicationWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PartyApplicationServiceTest {

    @Mock
    private PartyApplicationReader partyApplicationReader;

    @Mock
    private PartyApplicationWriter partyApplicationWriter;

    @InjectMocks
    private PartyApplicationService partyApplicationService;

    private PartyApplication testApplication;

    @BeforeEach
    void setUp() {
        testApplication = partyApplicationService.initForCreate(1L, 1L);
    }

    @Test
    @DisplayName("파티 지원서 생성 - 성공")
    void create_Success() {
        // given
        Long partyRecruitId = 1L;
        Long resumeId = 1L;
        Long expectedId = 1L;

        given(partyApplicationWriter.create(any(PartyApplication.class)))
                .willReturn(expectedId);

        // when
        long result = partyApplicationService.create(partyRecruitId, resumeId);

        // then
        assertThat(result).isEqualTo(expectedId);
        verify(partyApplicationWriter).create(any(PartyApplication.class));
    }

    @Test
    @DisplayName("파티 지원서 초기화 - 성공")
    void initForCreate_Success() {
        // given
        Long partyRecruitId = 1L;
        Long resumeId = 1L;

        // when
        PartyApplication result = partyApplicationService.initForCreate(partyRecruitId, resumeId);

        // then
        assertThat(result.partyRecruitId()).isEqualTo(partyRecruitId);
        assertThat(result.resumeId()).isEqualTo(resumeId);
        assertThat(result.revision()).isEqualTo(0L);
        assertThat(result.status()).isEqualTo(PartyApplicationStatusTypeEnum.PENDING);
        assertThat(result.metadata()).isNotNull();
    }

    @Test
    @DisplayName("지원서 ID로 조회 - 성공")
    void getByApplicationId_Success() {
        // given
        Long applicationId = 1L;

        given(partyApplicationReader.findById(applicationId))
                .willReturn(Optional.of(testApplication));

        // when
        PartyApplication result = partyApplicationService.getByApplicationId(applicationId);

        // then
        assertThat(result).isEqualTo(testApplication);
        verify(partyApplicationReader).findById(applicationId);
    }

    @Test
    @DisplayName("지원서 ID로 조회 - 존재하지 않는 경우 예외 발생")
    void getByApplicationId_NotFound_ThrowsException() {
        // given
        Long applicationId = 1L;

        given(partyApplicationReader.findById(applicationId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> partyApplicationService.getByApplicationId(applicationId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원서를 찾을 수 없습니다.");

        verify(partyApplicationReader).findById(applicationId);
    }

    @Test
    @DisplayName("지원서 ID로 조회 - Optional 반환 성공")
    void findById_Success() {
        // given
        Long applicationId = 1L;

        given(partyApplicationReader.findById(applicationId))
                .willReturn(Optional.of(testApplication));

        // when
        Optional<PartyApplication> result = partyApplicationService.findById(applicationId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testApplication);
        verify(partyApplicationReader).findById(applicationId);
    }

    @Test
    @DisplayName("지원서 ID로 조회 - Optional 빈 값 반환")
    void findById_NotFound_ReturnsEmpty() {
        // given
        Long applicationId = 1L;

        given(partyApplicationReader.findById(applicationId))
                .willReturn(Optional.empty());

        // when
        Optional<PartyApplication> result = partyApplicationService.findById(applicationId);

        // then
        assertThat(result).isEmpty();
        verify(partyApplicationReader).findById(applicationId);
    }

    @Test
    @DisplayName("파티 지원 여부 확인 - 이미 지원한 경우")
    void hasAppliedToParty_AlreadyApplied() {
        // given
        Long partyRecruitId = 1L;
        Long resumeId = 1L;

        given(partyApplicationReader.existsByPartyRecruitIdAndResumeId(partyRecruitId, resumeId))
                .willReturn(true);

        // when
        boolean result = partyApplicationService.hasAppliedToParty(partyRecruitId, resumeId);

        // then
        assertThat(result).isTrue();
        verify(partyApplicationReader).existsByPartyRecruitIdAndResumeId(partyRecruitId, resumeId);
    }

    @Test
    @DisplayName("파티 지원 여부 확인 - 지원하지 않은 경우")
    void hasAppliedToParty_NotApplied() {
        // given
        Long partyRecruitId = 1L;
        Long resumeId = 1L;

        given(partyApplicationReader.existsByPartyRecruitIdAndResumeId(partyRecruitId, resumeId))
                .willReturn(false);

        // when
        boolean result = partyApplicationService.hasAppliedToParty(partyRecruitId, resumeId);

        // then
        assertThat(result).isFalse();
        verify(partyApplicationReader).existsByPartyRecruitIdAndResumeId(partyRecruitId, resumeId);
    }
}