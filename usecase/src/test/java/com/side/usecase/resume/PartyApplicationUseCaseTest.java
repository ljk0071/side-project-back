package com.side.usecase.resume;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.service.PartyApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PartyApplicationUseCaseTest {

    @Mock
    private PartyApplicationService partyApplicationService;

    @InjectMocks
    private PartyApplicationUseCase partyApplicationUseCase;

    @Test
    @DisplayName("지원서 상태 변경 - 승인 상태로 변경")
    void changeStatus_ToAccepted() {
        // given
        long partyApplicationId = 1L;
        PartyApplicationStatusTypeEnum status = PartyApplicationStatusTypeEnum.ACCEPTED;

        // when
        String result = partyApplicationUseCase.changeStatus(partyApplicationId, status);

        // then
        assertThat(result).isEqualTo("승인됨으로 변경 되었습니다.");
        verify(partyApplicationService).changeStatus(partyApplicationId, status);
    }

    @Test
    @DisplayName("지원서 상태 변경 - 거절 상태로 변경")
    void changeStatus_ToRejected() {
        // given
        long partyApplicationId = 1L;
        PartyApplicationStatusTypeEnum status = PartyApplicationStatusTypeEnum.REJECTED;

        // when
        String result = partyApplicationUseCase.changeStatus(partyApplicationId, status);

        // then
        assertThat(result).isEqualTo("거부됨으로 변경 되었습니다.");
        verify(partyApplicationService).changeStatus(partyApplicationId, status);
    }

    @Test
    @DisplayName("지원서 상태 변경 - 취소 상태로 변경")
    void changeStatus_ToCanceled() {
        // given
        long partyApplicationId = 1L;
        PartyApplicationStatusTypeEnum status = PartyApplicationStatusTypeEnum.CANCELED;

        // when
        String result = partyApplicationUseCase.changeStatus(partyApplicationId, status);

        // then
        assertThat(result).isEqualTo("취소됨으로 변경 되었습니다.");
        verify(partyApplicationService).changeStatus(partyApplicationId, status);
    }

    @Test
    @DisplayName("지원서 상태 변경 - 대기 상태로 변경")
    void changeStatus_ToPending() {
        // given
        long partyApplicationId = 1L;
        PartyApplicationStatusTypeEnum status = PartyApplicationStatusTypeEnum.PENDING;

        // when
        String result = partyApplicationUseCase.changeStatus(partyApplicationId, status);

        // then
        assertThat(result).isEqualTo("대기중으로 변경 되었습니다.");
        verify(partyApplicationService).changeStatus(partyApplicationId, status);
    }
}