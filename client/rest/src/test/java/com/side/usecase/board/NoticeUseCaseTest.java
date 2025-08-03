package com.side.usecase.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.model.Article;
import com.side.domain.model.Notice;
import com.side.domain.service.NoticeService;

@ExtendWith(MockitoExtension.class)
class NoticeUseCaseTest {

	@Mock
	NoticeService noticeService;

	@InjectMocks
	private NoticeUseCase noticeUseCase;

	private Long validNoticeId;
	private Notice mockNotice;

	@BeforeEach
	void setUp() {
		// 테스트마다 공통적으로 사용할 값 초기화
		validNoticeId = -1L;

		mockNotice = Notice.builder()
						 .id(validNoticeId)
						 .viewCount(10L)
						 .article(Article.builder().title("테스트 제목").contents("테스트 본문").build())
						 .status(YesNoDeleteStatus.YES)
						 .metadata(Metadata.init())
						 .build();
	}

	@Test
	@DisplayName("findById 호출 시 공지사항을 반환하고, 조회수 증가 메서드가 비동기로 호출된다")
	void findById_returnsNoticeAndTriggersViewCountIncrease() throws Exception {
		// Given
		// Long noticeId = -1L;
		// Notice mockNotice = Notice.builder()
		// 						.id(noticeId)
		// 						.viewCount(10L)
		// 						.article(Article.builder().title("테스트 제목").contents("테스트 본문").build())
		// 						.status(YesNoDeleteStatus.YES)
		// 						.metadata(Metadata.init())
		// 						.build();

		when(noticeService.findById(validNoticeId)).thenReturn(Optional.of(mockNotice));

		// increaseViewCount는 void 메서드 → doNothing 사용 가능
		doNothing().when(noticeService).increaseViewCount(validNoticeId);

		// When
		Notice result = noticeUseCase.findById(validNoticeId); // useCase는 테스트 대상 클래스

		// Then
		assertNotNull(result);
		assertEquals(validNoticeId, result.id());
		assertEquals("테스트 제목", result.article().title());

		// 비동기 로직은 약간 기다려야 검증 가능함
		// → 500ms 정도 wait 후 verify
		Thread.sleep(500);
		verify(noticeService, timeout(1000).times(1)).increaseViewCount(validNoticeId);
	}

	@Test
	@DisplayName("findById 실패 시 IllegalArgumentException 예외가 발생한다")
	void findById_whenNotFound_throwsException() {
		when(noticeService.findById(validNoticeId)).thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			noticeUseCase.findById(validNoticeId);
		});

		// 예외 메시지 검증
		assertEquals("공지사항이 존재하지 않습니다.", ex.getMessage());

		verify(noticeService, times(1)).findById(validNoticeId);
		verify(noticeService, never()).increaseViewCount(anyLong());
	}

}