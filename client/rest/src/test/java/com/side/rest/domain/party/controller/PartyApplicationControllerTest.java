package com.side.rest.domain.party.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
import com.side.rest.domain.party.dto.request.PartyApplicationRequestDto;
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
@DisplayName("파티 지원서 테스트")
@ExtendWith(RestDocumentationExtension.class)
class PartyApplicationControllerTest {

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
    @DisplayName("파티 지원서 생성 - 성공")
    void create_Success() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        PartyApplicationRequestDto dto = new PartyApplicationRequestDto();
        dto.setPartyRecruitId(1L);
        dto.setResumeId(1L);

        this.mockMvc.perform(
                    post("/v1/party/application")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
            )
                    .andExpect(status().isOk())
                    .andDo(
                            document(
                                    "파티 지원서 등록하기",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("파티 지원서 정보")
                                                                      .summary("파티 지원서를 등록합니다.")
                                                                      .description("파티 모집글 ID와 이력서 ID를 전송받아 파티 지원서를 등록합니다.")
                                                                      .requestSchema(Schema.schema("파티 지원서 등록 요청"))
                                                                      .requestFields(
                                                                              fieldWithPath("id").type(JsonFieldType.NULL)
                                                                                                 .description("지원서 ID (등록 시 null)")
                                                                                                 .optional(),
                                                                              fieldWithPath("revision").type(JsonFieldType.NULL)
                                                                                                       .description("버전 (등록 시 null)")
                                                                                                       .optional(),
                                                                              fieldWithPath("partyRecruitId").type(JsonFieldType.NUMBER)
                                                                                                             .description("파티 모집글 ID"),
                                                                              fieldWithPath("resumeId").type(JsonFieldType.NUMBER)
                                                                                                       .description("이력서 ID"),
                                                                              fieldWithPath("status").type(JsonFieldType.NULL)
                                                                                                     .description("지원 상태 (등록 시 null, 자동으로 PENDING 설정)")
                                                                                                     .optional()
                                                                      )
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("파티 지원서 생성 - 필수 값 누락")
    void create_MissingRequiredFields() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        PartyApplicationRequestDto dto = new PartyApplicationRequestDto();
        // partyRecruitId와 resumeId를 설정하지 않음

        this.mockMvc.perform(
                    post("/v1/party/application")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
            )
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "파티 지원서 등록하기 - 필수 값 누락",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("파티 지원서 정보")
                                                                      .summary("파티 지원서 등록 실패 - 필수 값 누락")
                                                                      .description("필수 필드가 누락된 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("파티 지원서 등록 요청 - 실패"))
                                                                      .build())
                            )
                    );
    }

    @Test
    @Transactional
    @DisplayName("파티 지원서 생성 - 잘못된 값")
    void create_InvalidValues() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        PartyApplicationRequestDto dto = new PartyApplicationRequestDto();
        dto.setPartyRecruitId(-1L); // 음수 값
        dto.setResumeId(0L); // 0 값

        this.mockMvc.perform(
                    post("/v1/party/application")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
            )
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "파티 지원서 등록하기 - 잘못된 값",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("파티 지원서 정보")
                                                                      .summary("파티 지원서 등록 실패 - 잘못된 값")
                                                                      .description("잘못된 값이 전송된 경우 400 Bad Request를 반환합니다.")
                                                                      .requestSchema(Schema.schema("파티 지원서 등록 요청 - 잘못된 값"))
                                                                      .build())
                            )
                    );
    }
}