package com.side.rest.domain.board.controller;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
import com.side.rest.domain.board.dto.request.ArticleRequestDto;
import com.side.rest.domain.board.dto.request.NoticeRequestDto;
import com.side.rest.util.TestLoginUtil;

import jakarta.servlet.http.Cookie;

import com.navercorp.fixturemonkey.FixtureMonkey;

@SpringBootTest(classes = SideApplication.class)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@Transactional
class NoticeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JdbcClient jdbcClient;

	private FixtureMonkey fixtureMonkey;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
									  .apply(documentationConfiguration(restDocumentation).operationPreprocessors().withRequestDefaults(prettyPrint()).withResponseDefaults(prettyPrint()))
									  .apply(springSecurity())
									  .build();

		this.fixtureMonkey = FixtureMonkey.create();
	}

	@Test
	@DisplayName("공지사항 등록 - 성공")
	void createNotice_Success() throws Exception {

		// given: 공지사항 요청 DTO 구성
		ArticleRequestDto articleDto = new ArticleRequestDto();
		articleDto.setTitle("테스트 제목");
		articleDto.setContents("테스트 내용");

		NoticeRequestDto requestDto = new NoticeRequestDto();
		requestDto.setArticle(articleDto);
		// metadata는 아예 설정하지 않음. 서비스에서 자동 생성

		TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
		Map<String, String> loginResult = tlu.login();

		// when & then
		mockMvc.perform(post("/v1/notice").accept(MediaType.APPLICATION_JSON)
										  .contentType(MediaType.APPLICATION_JSON)
										  .header("X-CSRF-TOKEN", loginResult.get("csrfToken"))
										  .cookie(new Cookie("Authorization", loginResult.get("accessToken")))
										  .content(objectMapper.writeValueAsString(requestDto)))
			   .andExpect(status().isOk())
			   .andDo(document("공지사항 등록", resource(ResourceSnippetParameters.builder()
													   .tag("공지사항")
													   .summary("공지사항 등록")
													   .description("공지사항 제목과 내용을 입력하여 공지사항을 등록합니다.")
													   .requestFields(fieldWithPath("id").optional().description("공지사항 ID (등록 시에는 null이어야 함)"),
																	  fieldWithPath("article").type(JsonFieldType.OBJECT).description("공지사항 본문"),
																	  fieldWithPath("article.title").type(JsonFieldType.STRING).description("제목"),
																	  fieldWithPath("article.contents").type(JsonFieldType.STRING).description("내용"),
																	  fieldWithPath("metadata").type(JsonFieldType.NULL).description("메타데이터 (서버에서 자동 생성)").optional())
													   .build())));
	}

	@Test
	@DisplayName("공지사항 등록 - 실패 (제목과 내용이 null)")
	void createNotice_Fail_NullTitleAndContents() throws Exception {
		// given
		ArticleRequestDto articleDto = new ArticleRequestDto();
		articleDto.setTitle(null);  // 제목 null
		articleDto.setContents(null); // 내용 null

		NoticeRequestDto requestDto = new NoticeRequestDto();
		requestDto.setArticle(articleDto);

		TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
		Map<String, String> loginResult = tlu.login();

		// when & then
		mockMvc.perform(post("/v1/notice")
							.accept(MediaType.APPLICATION_JSON)
							.contentType(MediaType.APPLICATION_JSON)
							.header("X-CSRF-TOKEN", loginResult.get("csrfToken"))
							.cookie(new Cookie("Authorization", loginResult.get("accessToken")))
							.content(objectMapper.writeValueAsString(requestDto)))
			   .andExpect(status().isBadRequest())
			   .andExpect(jsonPath("$.message").exists());
	}

	@DisplayName("공지사항 조회 - 성공")
	@Test
	void findNotice_Success() throws Exception {
		// given
		String keyword = "테스트";
		String type = "TITLE"; // enum NoticeSearchType 의 기본값

		TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
		Map<String, String> loginResult = tlu.login();

		// when & then
		mockMvc.perform(get("/v1/notice")
							.param("keyword", keyword)
							.param("type", type)
							.header("X-CSRF-TOKEN", loginResult.get("csrfToken"))
							.cookie(new Cookie("Authorization", loginResult.get("accessToken")))
							.accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andDo(document("공지사항 조회 - 성공",
							   resource(ResourceSnippetParameters.builder()
											.tag("공지사항")
											.summary("공지사항 조회")
											.description("키워드와 타입으로 공지사항을 조회합니다.")
											.queryParameters(
												parameterWithName("keyword").description("검색 키워드"),
												parameterWithName("type").description("검색 타입 (예: all, title, contents)")
											)
											.responseFields(
												fieldWithPath("[].id").description("공지사항 ID"),
												fieldWithPath("[].revision").description("수정 번호"),
												fieldWithPath("[].viewCount").description("조회수"),
												fieldWithPath("[].status").description("공지 상태 (예: YES, NO)"),

												fieldWithPath("[].article.title").description("공지사항 제목"),
												fieldWithPath("[].article.contents").description("공지사항 내용"),

												fieldWithPath("[].metadata.createdByName").description("작성자 이름").optional(),
												fieldWithPath("[].metadata.createdAt").description("작성일시 (timestamp)").optional(),
												fieldWithPath("[].metadata.modifiedByName").description("수정자 이름").optional(),
												fieldWithPath("[].metadata.modifiedAt").description("수정일시").optional(),
												fieldWithPath("[].metadata.deletedByName").description("삭제자 이름").optional(),
												fieldWithPath("[].metadata.deletedAt").description("삭제일시").optional()
											)

											.build()
							   )
			   ));
	}

	@DisplayName("공지사항 조회 - 실패 (keyword 누락)")
	@Test
	void findNotice_Fail_KeywordMissing() throws Exception {
		// given
		String type = "TITLE";

		TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
		Map<String, String> loginResult = tlu.login();

		// when & then
		mockMvc.perform(get("/v1/notice")
							// .param("keyword", null) // 일부러 keyword 파라미터 자체를 생략함
							.param("type", type)
							.header("X-CSRF-TOKEN", loginResult.get("csrfToken"))
							.cookie(new Cookie("Authorization", loginResult.get("accessToken")))
							.accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isBadRequest()) // 400 Bad Request
			   .andExpect(jsonPath("$.message").exists())
			   .andDo(document("공지사항 조회 - 실패 - 키워드 누락",
							   resource(ResourceSnippetParameters.builder()
											.tag("공지사항")
											.summary("공지사항 조회 실패 - 키워드 누락")
											.description("keyword 파라미터가 누락된 경우 실패합니다.")
											.queryParameters(
												parameterWithName("type").description("검색 타입").optional()
											)
											.responseFields(
												fieldWithPath("message").description("에러 메시지")
											)
											.build()
							   )));
	}

	@Test
	@DisplayName("공지사항 단건 조회 - 성공")
	void findNoticeById_Success() throws Exception {

		createNotice_Success();

		Object noticeId = jdbcClient.sql("select id from notice limit 1")
									.query()
									.singleValue();

		// given
		TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
		Map<String, String> loginResult = tlu.login();

		// when & then
		mockMvc.perform(get("/v1/notice/{id}", noticeId)
							.accept(MediaType.APPLICATION_JSON)
							.contentType(MediaType.APPLICATION_JSON)
							.header("X-CSRF-TOKEN", loginResult.get("csrfToken"))
							.cookie(new Cookie("Authorization", loginResult.get("accessToken"))))
			   .andExpect(status().isOk());
	}

	@Test
	@DisplayName("공지사항 단건 조회 - 실패 (존재하지 않는 ID)")
	void findNoticeById_Fail_NotFound() throws Exception {
		// given
		Long invalidNoticeId = -999L;

		TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
		Map<String, String> loginResult = tlu.login();

		// when & then
		mockMvc.perform(get("/v1/notice/{id}", invalidNoticeId)
							.accept(MediaType.APPLICATION_JSON)
							.contentType(MediaType.APPLICATION_JSON)
							.header("X-CSRF-TOKEN", loginResult.get("csrfToken"))
							.cookie(new Cookie("Authorization", loginResult.get("accessToken"))))
			   .andExpect(status().isBadRequest());
	}

}
