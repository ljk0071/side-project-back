package com.side.rest.domain.board;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
import com.side.rest.domain.board.dto.request.ArticleRequestDto;
import com.side.rest.domain.board.dto.request.PartyRecruitRequestDto;
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
@DisplayName("파티 모집 테스트")
@ExtendWith(RestDocumentationExtension.class)
class PartyRecruitControllerTest {

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
    void create() throws Exception {

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
                                                                             fieldWithPath("maxMembers").type(JsonFieldType.NUMBER)
                                                                                                        .description("최대 멤버수")
                                                                     )
                                                                     .build()
                                    )
                            )
                    );
    }
}