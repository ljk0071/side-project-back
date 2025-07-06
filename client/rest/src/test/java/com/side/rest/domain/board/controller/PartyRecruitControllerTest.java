package com.side.rest.domain.board.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.side.bootstrap.SideApplication;
import com.side.domain.enums.SearchType;
import com.side.rest.domain.board.dto.request.ArticleRequestDto;
import com.side.rest.domain.board.dto.request.PartyRecruitRequestDto;
import com.side.rest.domain.board.dto.request.SearchRequestDto;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SideApplication.class)
@AutoConfigureMockMvc
@DisplayName("파티 모집 테스트")
@ExtendWith(RestDocumentationExtension.class)
@Transactional
class PartyRecruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                      .apply(documentationConfiguration(restDocumentation)
                                              .operationPreprocessors()
                                              .withRequestDefaults(prettyPrint())
                                              .withResponseDefaults(prettyPrint()))
                                      .apply(springSecurity())
                                      .build();

        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("파티 모집글 생성 - 성공")
    void create_Success() throws Exception {

        PartyRecruitRequestDto dto = new PartyRecruitRequestDto();
        ArticleRequestDto articleRequestDto = new ArticleRequestDto();

        articleRequestDto.setTitle("test");
        articleRequestDto.setContents("testBody");
        dto.setArticle(articleRequestDto);

        dto.setMaxMembers(2);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/party")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "게시글 등록하기",
                                    resource(
                                            ResourceSnippetParameters.builder()
                                                                     .tag("게시글 정보")
                                                                     .summary("게시글을 등록합니다.")
                                                                     .description("제목과 내용을 전송받아 게시글을 등록합니다.")
                                                                     .requestSchema(Schema.schema("게시글 등록 요청"))
                                                                     .requestFields(
                                                                             fieldWithPath("id").type(JsonFieldType.NULL)
                                                                                                .description("게시글 ID (등록 시 null)")
                                                                                                .optional(),
                                                                             fieldWithPath("article").type(JsonFieldType.OBJECT)
                                                                                                     .description("게시글 정보"),
                                                                             fieldWithPath("article.title").type(
                                                                                                                   JsonFieldType.STRING)
                                                                                                           .description("게시글 제목"),
                                                                             fieldWithPath("article.contents").type(
                                                                                                                      JsonFieldType.STRING)
                                                                                                              .description(
                                                                                                                      "게시글 내용"),
                                                                             fieldWithPath("userUniqueId").type(JsonFieldType.NULL)
                                                                                                          .description("유저 고유 id입니다. 해당 값은 전달받은 token으로 서버 내부에서 처리합니다."),
                                                                             fieldWithPath("status").type(JsonFieldType.NULL)
                                                                                                    .description("모집글 상태입니다. 해당 값은 서버 내부에서 처리합니다."),
                                                                             fieldWithPath("maxMembers").type(JsonFieldType.NUMBER)
                                                                                                        .description("최대 멤버수")
                                                                     )
                                                                     .build()
                                    )
                            )
                    );
    }

    @Test
    @DisplayName("파티 모집글 생성 - 게시글 정보 누락")
    void create_MissingArticle() throws Exception {

        PartyRecruitRequestDto dto = fixtureMonkey.giveMeOne(PartyRecruitRequestDto.class);
        dto.setArticle(null);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/party")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "게시글 등록하기 - 게시글 정보 누락",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("파티 모집글 등록 실패 - 게시글 정보 누락")
                                                                      .description("게시글 정보가 누락된 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("게시글 등록 요청 - 실패"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("파티 모집글 생성 - 최대 인원 누락")
    void create_MissingMaxMembers() throws Exception {

        PartyRecruitRequestDto dto = fixtureMonkey.giveMeOne(PartyRecruitRequestDto.class);

        // maxMembers를 설정하지 않음
        dto.setMaxMembers(null);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/party")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "게시글 등록하기 - 최대 인원 누락",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("파티 모집글 등록 실패 - 최대 인원 누락")
                                                                      .description("최대 인원이 누락된 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("게시글 등록 요청 - 최대 인원 누락"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("파티 모집글 생성 - 최대 인원 범위 초과")
    void create_InvalidMaxMembers() throws Exception {

        PartyRecruitRequestDto dto = fixtureMonkey.giveMeOne(PartyRecruitRequestDto.class);

        // 최대 6명 초과
        dto.setMaxMembers(10);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/party")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "게시글 등록하기 - 최대 인원 범위 초과",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("파티 모집글 등록 실패 - 최대 인원 범위 초과")
                                                                      .description("최대 인원이 허용 범위(2-6명)를 벗어난 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("게시글 등록 요청 - 범위 초과"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("파티 모집글 생성 - 게시글 제목/내용 누락")
    void create_MissingTitleAndContents() throws Exception {

        PartyRecruitRequestDto dto = fixtureMonkey.giveMeOne(PartyRecruitRequestDto.class);
        dto.getArticle().setTitle(null);
        dto.getArticle().setContents(null);

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        this.mockMvc.perform(post("/v1/party")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "게시글 등록하기 - 제목 내용 누락",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("파티 모집글 등록 실패 - 제목/내용 누락")
                                                                      .description("게시글 제목이나 내용이 누락된 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("게시글 등록 요청 - 제목 내용 누락"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("활성 모집글 조회 - 전체 검색")
    void getActiveRecruits_Success() throws Exception {
        // given
        SearchRequestDto searchDto = fixtureMonkey.giveMeBuilder(SearchRequestDto.class)
                                                  .set("searchConditions", List.of(SearchType.ALL))
                                                  .set("searchKeyword", "파티")
                                                  .sample();

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
        Map<String, String> result = tlu.login();

        // when & then
        MockHttpServletRequestBuilder mockMvcBuilder = get("/v1/party")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-CSRF-TOKEN", result.get("csrfToken"))
                .cookie(new Cookie("Authorization", result.get("accessToken")));

        searchDto.getSearchConditions()
                 .forEach(condition -> mockMvcBuilder.queryParam("searchConditions", condition.name()));

        this.mockMvc.perform(mockMvcBuilder
                    .queryParam("searchKeyword", searchDto.getSearchKeyword()))
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "활성 모집글 조회 - 성공",
                                    resource(
                                            ResourceSnippetParameters.builder()
                                                                     .tag("게시글 정보")
                                                                     .summary("활성 모집글을 검색합니다.")
                                                                     .description("검색 조건과 키워드로 활성 상태의 파티 모집글을 조회합니다.")
                                                                     .queryParameters(
                                                                             parameterWithName("searchConditions")
                                                                                     .description("검색 조건 (ALL, TITLE, CONTENTS)")
                                                                                     .optional(),
                                                                             parameterWithName("searchKeyword")
                                                                                     .description("검색 키워드 (1-100자)")
                                                                                     .optional()
                                                                     )
                                                                     .build()
                                    )
                            )
                    );
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: TITLE 검색")
    void getActiveRecruits_Success_TitleSearch() throws Exception {
        // given
        SearchRequestDto searchDto = fixtureMonkey.giveMeBuilder(SearchRequestDto.class)
                                                  .set("searchConditions", List.of(SearchType.TITLE))
                                                  .set("searchKeyword", "제목검색")
                                                  .sample();

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
        Map<String, String> result = tlu.login();

        // when & then
        MockHttpServletRequestBuilder mockMvcBuilder = get("/v1/party")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-CSRF-TOKEN", result.get("csrfToken"))
                .cookie(new Cookie("Authorization", result.get("accessToken")));

        searchDto.getSearchConditions()
                 .forEach(condition -> mockMvcBuilder.queryParam("searchConditions", condition.name()));

        this.mockMvc.perform(mockMvcBuilder
                    .queryParam("searchKeyword", searchDto.getSearchKeyword()))
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "활성 모집글 조회 - 제목 검색",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("제목으로 활성 모집글 검색")
                                                                      .description("제목 필드에서 키워드를 검색하여 활성 모집글을 조회합니다.")
                                                                      .queryParameters(
                                                                              parameterWithName("searchConditions")
                                                                                      .description("검색 조건 (TITLE)")
                                                                                      .optional(),
                                                                              parameterWithName("searchKeyword")
                                                                                      .description("검색 키워드")
                                                                                      .optional()
                                                                      )
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("활성 모집글 조회 - 성공: 여러 검색 조건")
    void getActiveRecruits_Success_MultipleConditions() throws Exception {
        // given
        SearchRequestDto searchDto = fixtureMonkey.giveMeBuilder(SearchRequestDto.class)
                                                  .set("searchConditions", List.of(SearchType.TITLE, SearchType.CONTENTS))
                                                  .set("searchKeyword", "검색어")
                                                  .sample();

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
        Map<String, String> result = tlu.login();

        // when & then
        MockHttpServletRequestBuilder mockMvcBuilder = get("/v1/party")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-CSRF-TOKEN", result.get("csrfToken"))
                .cookie(new Cookie("Authorization", result.get("accessToken")));

        searchDto.getSearchConditions()
                 .forEach(condition -> mockMvcBuilder.queryParam("searchConditions", condition.name()));

        this.mockMvc.perform(mockMvcBuilder
                    .queryParam("searchKeyword", searchDto.getSearchKeyword()))
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "활성 모집글 조회 - 복합 검색",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("여러 조건으로 활성 모집글 검색")
                                                                      .description("제목과 내용에서 키워드를 검색하여 활성 모집글을 조회합니다.")
                                                                      .queryParameters(
                                                                              parameterWithName("searchConditions")
                                                                                      .description("검색 조건 (ALL, TITLE, CONTENTS)")
                                                                                      .optional(),
                                                                              parameterWithName("searchKeyword")
                                                                                      .description("검색 키워드")
                                                                                      .optional()
                                                                      )
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("활성 모집글 조회 - 실패: 검색 조건 누락")
    void getActiveRecruits_Fail_EmptySearchConditions() throws Exception {
        // given
        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
        Map<String, String> result = tlu.login();

        // when & then
        this.mockMvc.perform(get("/v1/party")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .queryParam("searchKeyword", "검색어"))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "활성 모집글 조회 - 검색 조건 누락",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("활성 모집글 검색 실패 - 검색 조건 누락")
                                                                      .description("검색 조건이 누락된 경우 400 Bad Request를 반환합니다.")
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("활성 모집글 조회 - 실패: 검색 키워드 길이 초과")
    void getActiveRecruits_Fail_KeywordTooLong() throws Exception {
        // given
        String longKeyword = "a".repeat(101); // 100자 초과
        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
        Map<String, String> result = tlu.login();

        // when & then
        this.mockMvc.perform(get("/v1/party")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .queryParam("searchConditions", "ALL")
                    .queryParam("searchKeyword", longKeyword))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "활성 모집글 조회 - 키워드 길이 초과",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("활성 모집글 검색 실패 - 키워드 길이 초과")
                                                                      .description("검색 키워드가 100자를 초과한 경우 400 Bad Request를 반환합니다.")
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("활성 모집글 조회 - 실패: 검색 키워드 빈 문자열")
    void getActiveRecruits_Fail_EmptyKeyword() throws Exception {
        // given
        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);
        Map<String, String> result = tlu.login();

        // when & then
        this.mockMvc.perform(get("/v1/party")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-CSRF-TOKEN", result.get("csrfToken"))
                    .cookie(new Cookie("Authorization", result.get("accessToken")))
                    .queryParam("searchConditions", "ALL")
                    .queryParam("searchKeyword", ""))
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "활성 모집글 조회 - 빈 키워드",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("게시글 정보")
                                                                      .summary("활성 모집글 검색 실패 - 빈 키워드")
                                                                      .description("검색 키워드가 빈 문자열인 경우 400 Bad Request를 반환합니다.")
                                                                      .build())
                            )
                    );
    }


}