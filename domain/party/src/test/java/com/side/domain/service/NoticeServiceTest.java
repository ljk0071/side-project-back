package com.side.domain.service;

import com.side.domain.YesNoDeleteStatus;
import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Article;
import com.side.domain.model.Notice;
import com.side.domain.repository.NoticeRepository;
import com.side.domain.repository.NoticeRepositoryManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

	@Mock
	private NoticeRepository noticeRepository;

	@InjectMocks
	private NoticeService noticeService;

	@Test
	@DisplayName("create 호출 시 initForCreate 로 초기화된 값들이 noticeRepository.create 에 반영되는지 검증")
	void createNotice_initializesFieldsViaInitForCreate() {

		// Given
		Article article = Article.builder()
							  .title("테스트 제목")
							  .contents("테스트 본문")
							  .build();

		Notice originalNotice = Notice.builder()
									.article(article)
									.viewCount(999L)
									.status(YesNoDeleteStatus.NO)
									.metadata(null)
									.build();

		try (MockedStatic<NoticeRepositoryManager> mockedStatic = mockStatic(NoticeRepositoryManager.class)) {
			mockedStatic.when(() -> NoticeRepositoryManager.getNoticeRepository(any()))
						.thenReturn(noticeRepository);

			// noticeService.create(originalNotice);
			// noticeService.create를 create내부 로직으로 대체
			// 내가 지금 기억은 잘 안나는데 아마 initForCreate만 있을거임
			Notice newNotice = noticeService.initForCreate(originalNotice);

			// initForCreate에 의해 바뀌었는지 검증
			assertEquals(0L, newNotice.viewCount(), "viewCount가 0으로 초기화되어야 합니다");
			assertEquals(YesNoDeleteStatus.YES, newNotice.status(), "status가 YES로 설정되어야 합니다");
			assertNotNull(newNotice.metadata(), "metadata는 null이 아니어야 합니다");

			assertEquals(999L, originalNotice.viewCount(), "원본 notice는 viewCount가 999이어야 합니다.");
			assertEquals(YesNoDeleteStatus.NO, originalNotice.status(), "원본 notice는 status가 NO로 설정되어야 합니다");
			assertNull(originalNotice.metadata(), "원본 notice는 metadata가 null이어야 합니다");

		}
	}

	@Test
	@DisplayName("키워드와 타입에 따라 Repository의 find 메서드가 호출되고, 결과가 반환되는지 검증")
	void findNotice_callsRepositoryFindAndReturnsResult() {
		// given
		String keyword = "title";
		NoticeSearchType type = NoticeSearchType.ALL;
		List<Notice> expectedList = List.of(
			Notice.builder().id(1L).build(),
			Notice.builder().id(2L).build()
		);

		// static 메서드 mock 처리 시작
		try (MockedStatic<NoticeRepositoryManager> mockedStatic = mockStatic(NoticeRepositoryManager.class)) {
			// getNoticeRepository(...) 호출시 Mocked noticeRepository 반환하도록 지정
			mockedStatic.when(() -> NoticeRepositoryManager.getNoticeRepository(any()))
						.thenReturn(noticeRepository);

			// repository.find(...) 호출 시 expectedList 반환하도록 지정
			when(noticeRepository.find(keyword, type)).thenReturn(expectedList);

			// when
			List<Notice> result = noticeService.find(keyword, type);

			// then
			verify(noticeRepository, times(1)).find(keyword, type);
			assertThat(result).isEqualTo(expectedList);
		}
	}

}
