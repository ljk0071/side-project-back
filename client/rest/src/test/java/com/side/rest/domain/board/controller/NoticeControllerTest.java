package com.side.rest.domain.board.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
import com.side.rest.domain.board.dto.request.ArticleRequestDto;
import com.side.rest.domain.board.dto.request.NoticeRequestDto;
import com.side.rest.util.TestLoginUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SideApplication.class)
@AutoConfigureMockMvc
@DisplayName("공지사항 테스트")
@ExtendWith(RestDocumentationExtension.class)
class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                      .apply(documentationConfiguration(restDocumentation)
                                              .operationPreprocessors()
                                              .withRequestDefaults(prettyPrint())
                                              .withResponseDefaults(prettyPrint()))
                                      .apply(springSecurity())
                                      .build();
    }

    @Test
    @Transactional
    @DisplayName("공지사항 생성 - 성공")
    void create_Success() throws Exception {

        NoticeRequestDto dto = new NoticeRequestDto();
        ArticleRequestDto articleRequestDto = new ArticleRequestDto();

        articleRequestDto.setTitle("공지사항 제목");
        articleRequestDto.setContents("공지사항 내용입니다.");
        dto.setArticle(articleRequestDto);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/notice")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "공지사항 등록하기",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("공지사항 정보")
                                                                      .summary("공지사항을 등록합니다.")
                                                                      .description("제목과 내용을 전송받아 공지사항을 등록합니다.")
                                                                      .requestSchema(Schema.schema("공지사항 등록 요청"))
                                                                      .requestFields(
                                                                              fieldWithPath("id").type(JsonFieldType.NULL)
                                                                                                 .description("공지사항 ID (등록 시 null)")
                                                                                                 .optional(),
                                                                              fieldWithPath("article").type(JsonFieldType.OBJECT)
                                                                                                      .description("게시글 정보"),
                                                                              fieldWithPath("article.title").type(JsonFieldType.STRING)
                                                                                                            .description("공지사항 제목"),
                                                                              fieldWithPath("article.contents").type(JsonFieldType.STRING)
                                                                                                               .description("공지사항 내용")
                                                                      )
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("공지사항 생성 - 게시글 정보 누락")
    void create_MissingArticle() throws Exception {

        NoticeRequestDto dto = new NoticeRequestDto();
        // article을 설정하지 않음

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/notice")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "공지사항 등록하기 - 게시글 정보 누락",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("공지사항 정보")
                                                                      .summary("공지사항 등록 실패 - 게시글 정보 누락")
                                                                      .description("게시글 정보가 누락된 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("공지사항 등록 요청 - 실패"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("공지사항 생성 - 제목/내용 누락")
    void create_MissingTitleAndContents() throws Exception {

        NoticeRequestDto dto = new NoticeRequestDto();
        ArticleRequestDto articleRequestDto = new ArticleRequestDto();
        // title과 contents를 설정하지 않음
        dto.setArticle(articleRequestDto);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/notice")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "공지사항 등록하기 - 제목 내용 누락",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("공지사항 정보")
                                                                      .summary("공지사항 등록 실패 - 제목/내용 누락")
                                                                      .description("제목이나 내용이 누락된 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("공지사항 등록 요청 - 제목 내용 누락"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("공지사항 생성 - 제목 길이 초과")
    void create_TitleTooLong() throws Exception {

        NoticeRequestDto dto = new NoticeRequestDto();
        ArticleRequestDto articleRequestDto = new ArticleRequestDto();

        articleRequestDto.setTitle("a".repeat(101)); // 100자 초과
        articleRequestDto.setContents("공지사항 내용입니다.");
        dto.setArticle(articleRequestDto);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/notice")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "공지사항 등록하기 - 제목 길이 초과",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("공지사항 정보")
                                                                      .summary("공지사항 등록 실패 - 제목 길이 초과")
                                                                      .description("제목이 100자를 초과한 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("공지사항 등록 요청 - 제목 길이 초과"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("공지사항 생성 - 내용 길이 초과")
    void create_ContentsTooLong() throws Exception {

        NoticeRequestDto dto = new NoticeRequestDto();
        ArticleRequestDto articleRequestDto = new ArticleRequestDto();

        articleRequestDto.setTitle("공지사항 제목");
        articleRequestDto.setContents("a".repeat(256)); // 255자 초과
        dto.setArticle(articleRequestDto);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/notice")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "공지사항 등록하기 - 내용 길이 초과",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("공지사항 정보")
                                                                      .summary("공지사항 등록 실패 - 내용 길이 초과")
                                                                      .description("내용이 255자를 초과한 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("공지사항 등록 요청 - 내용 길이 초과"))
                                                                      .build())
                            )
                    );
    }
}
