package com.side.rest.domain.party.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
import com.side.rest.domain.party.dto.request.ResumeRequestDto;
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
@DisplayName("이력서 테스트")
@ExtendWith(RestDocumentationExtension.class)
class ResumeControllerTest {

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
    @DisplayName("이력서 생성 - 성공")
    void create_Success() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        ResumeRequestDto dto = new ResumeRequestDto();
        dto.setContents("""
                안녕하세요. 저는 백엔드 개발자입니다.
                
                - 경력: 5년
                - 주요 기술 스택: Java, Spring Boot, MySQL
                - 프로젝트 경험: 전자상거래 플랫폼 개발
                """);

        this.mockMvc.perform(
                    post("/v1/resume")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
            )
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "이력서 등록하기",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("이력서 정보")
                                                                      .summary("이력서를 등록합니다.")
                                                                      .description("이력서 내용을 전송받아 이력서를 등록합니다.")
                                                                      .requestSchema(Schema.schema("이력서 등록 요청"))
                                                                      .requestFields(
                                                                              fieldWithPath("id").type(JsonFieldType.NULL)
                                                                                                 .description("이력서 ID (등록 시 null)"),
                                                                              fieldWithPath("userUniqueId").type(JsonFieldType.NULL)
                                                                                                           .description("사용자 고유 ID (자동 설정)"),
                                                                              fieldWithPath("status").type(JsonFieldType.NULL)
                                                                                                     .description("상태 (자동 설정)"),
                                                                              fieldWithPath("contents").type(JsonFieldType.STRING)
                                                                                                       .description("이력서 내용")
                                                                      )
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("이력서 생성 - 내용 누락")
    void create_MissingContents() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        ResumeRequestDto dto = new ResumeRequestDto();
        // contents를 설정하지 않음

        this.mockMvc.perform(
                    post("/v1/resume")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
            )
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "이력서 등록하기 - 내용 누락",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("이력서 정보")
                                                                      .summary("이력서 등록 실패 - 내용 누락")
                                                                      .description("이력서 내용이 누락된 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("이력서 등록 요청 - 실패"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("이력서 생성 - 내용 길이 초과")
    void create_ContentsTooLong() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        ResumeRequestDto dto = new ResumeRequestDto();
        dto.setContents("a".repeat(256)); // 255자 초과

        this.mockMvc.perform(
                    post("/v1/resume")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
            )
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "이력서 등록하기 - 내용 길이 초과",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("이력서 정보")
                                                                      .summary("이력서 등록 실패 - 내용 길이 초과")
                                                                      .description("이력서 내용이 255자를 초과한 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("이력서 등록 요청 - 길이 초과"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("이력서 생성 - 빈 내용")
    void create_EmptyContents() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        ResumeRequestDto dto = new ResumeRequestDto();
        dto.setContents(""); // 빈 문자열

        this.mockMvc.perform(
                    post("/v1/resume")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
            )
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "이력서 등록하기 - 빈 내용",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("이력서 정보")
                                                                      .summary("이력서 등록 실패 - 빈 내용")
                                                                      .description("이력서 내용이 빈 문자열인 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("이력서 등록 요청 - 빈 내용"))
                                                                      .build())
                            )
                    );
    }

}