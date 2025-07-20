package com.side.rest.domain.party.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
import com.side.domain.model.Article;
import com.side.domain.model.PartyRecruit;
import com.side.domain.model.Resume;
import com.side.domain.model.User;
import com.side.domain.service.PartyApplicationService;
import com.side.domain.service.PartyRecruitService;
import com.side.domain.service.ResumeService;
import com.side.rest.domain.party.dto.request.PartyApplicationRequestDto;
import com.side.rest.util.TestLoginUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Autowired
    private PartyRecruitService partyRecruitService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private PartyApplicationService service;

    @Autowired
    private JdbcClient jdbcClient;

    private List<Long> partyRecruitIds = new CopyOnWriteArrayList<>();
    private List<Long> resumeIds = new CopyOnWriteArrayList<>();

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                      .apply(documentationConfiguration(restDocumentation)
                                              .operationPreprocessors()
                                              .withRequestDefaults(prettyPrint())
                                              .withResponseDefaults(prettyPrint()))
                                      .apply(springSecurity())
                                      .build();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(User.builder()
                                                                                                         .uniqueId(1L)
                                                                                                         .build(), null));

        // 테스트용 PartyRecruit 생성
        partyRecruitIds.add(partyRecruitService.create(
                partyRecruitService.initForCreate(
                        PartyRecruit.builder()
                                    .article(Article.builder()
                                                    .title("테스트 파티 모집")
                                                    .contents("테스트용 파티 모집 내용입니다.")
                                                    .build())
                                    .userUniqueId(1L)
                                    .maxMembers(5)
                                    .build()
                )
        ));

        // 테스트용 Resume 생성
        resumeIds.add(resumeService.create(
                resumeService.initForCreate(
                        Resume.builder()
                              .contents("테스트용 이력서 내용입니다.")
                              .userUniqueId(1L)
                              .build()
                )
        ));
    }

    @AfterEach
    void tearDown() {

        jdbcClient.sql("""
                          DELETE FROM party_application
                          WHERE party_recruit_id = (:partyRecruitId)
                          and resume_id = (:resumeId)
                          """)
                  .param("partyRecruitId", partyRecruitIds.getLast())
                  .param("resumeId", resumeIds.getLast())
                  .update();

        jdbcClient.sql("""
                          DELETE FROM party_recruit
                          WHERE id IN (:partyRecruitIds)
                          """)
                  .param("partyRecruitIds", partyRecruitIds)
                  .update();

        jdbcClient.sql("""
                          DELETE FROM resume
                          WHERE id IN (:resumeIds)
                          """)
                  .param("resumeIds", resumeIds)
                  .update();
    }

    @Test
    @DisplayName("파티 지원서 생성 - 성공")
    void create_Success() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        PartyApplicationRequestDto dto = new PartyApplicationRequestDto();
        dto.setPartyRecruitId(partyRecruitIds.getLast());
        dto.setResumeId(resumeIds.getLast());

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

    @Test
    @DisplayName("파티 지원서 상태 변경 - 성공")
    void changeStatus_Success() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        // 먼저 파티 지원서를 생성
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(User.builder()
                                                                                                         .uniqueId(1L)
                                                                                                         .build(), null));
        long partyApplicationId = service.create(partyRecruitIds.getLast(), resumeIds.getLast());

        // 상태 변경 테스트 - 실제 생성된 지원서 ID를 사용하기 위해 생성 직후 ID 추출
        this.mockMvc.perform(
                    patch("/v1/party/application/" + partyApplicationId + "/ACCEPTED")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
            )
                    .andExpect(status().isOk())
                    .andExpect(content().string("승인됨으로 변경 되었습니다."))
                    .andDo(
                            document(
                                    "파티 지원서 상태 변경하기",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("파티 지원서 정보")
                                                                      .summary("파티 지원서 상태를 변경합니다.")
                                                                      .description("파티 지원서 ID와 상태를 전송받아 지원서 상태를 변경합니다.")
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("파티 지원서 상태 변경 - 거절")
    void changeStatus_Rejected() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        // 먼저 파티 지원서를 생성
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(User.builder()
                                                                                                         .uniqueId(1L)
                                                                                                         .build(), null));
        long partyApplicationId = service.create(partyRecruitIds.getLast(), resumeIds.getLast());

        // 거절 상태로 변경 테스트
        this.mockMvc.perform(
                    patch("/v1/party/application/" + partyApplicationId + "/REJECTED")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
            )
                    .andExpect(status().isOk())
                    .andExpect(content().string("거부됨으로 변경 되었습니다."))
                    .andDo(
                            document(
                                    "파티 지원서 상태 변경하기 - 거절",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("파티 지원서 정보")
                                                                      .summary("파티 지원서 상태를 거절로 변경합니다.")
                                                                      .description("파티 지원서를 거절 상태로 변경합니다.")
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("파티 지원서 상태 변경 - 취소")
    void changeStatus_Canceled() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        // 먼저 파티 지원서를 생성
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(User.builder()
                                                                                                         .uniqueId(1L)
                                                                                                         .build(), null));
        long partyApplicationId = service.create(partyRecruitIds.getLast(), resumeIds.getLast());

        // 취소 상태로 변경 테스트
        this.mockMvc.perform(
                    patch("/v1/party/application/" + partyApplicationId + "/CANCELED")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
            )
                    .andExpect(status().isOk())
                    .andExpect(content().string("취소됨으로 변경 되었습니다."))
                    .andDo(
                            document(
                                    "파티 지원서 상태 변경하기 - 취소",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("파티 지원서 정보")
                                                                      .summary("파티 지원서 상태를 취소로 변경합니다.")
                                                                      .description("파티 지원서를 취소 상태로 변경합니다.")
                                                                      .build())
                            )
                    );
    }

    @Test
    @DisplayName("파티 지원서 상태 변경 - 존재하지 않는 지원서")
    void changeStatus_NotFound() throws Exception {

        TestLoginUtil tlu = new TestLoginUtil(mockMvc, objectMapper);

        Map<String, String> result = tlu.login();

        // 존재하지 않는 지원서 ID로 상태 변경 시도
        this.mockMvc.perform(
                    patch("/v1/party/application/999/ACCEPTED")
                            .accept(MediaType.APPLICATION_JSON)
                            .header("X-CSRF-TOKEN", result.get("csrfToken"))
                            .cookie(new Cookie("Authorization", result.get("accessToken")))
            )
                    .andExpect(status().isBadRequest())
                    .andDo(
                            document(
                                    "파티 지원서 상태 변경하기 - 존재하지 않는 지원서",
                                    resource(ResourceSnippetParameters.builder()
                                                                      .tag("파티 지원서 정보")
                                                                      .summary("파티 지원서 상태 변경 실패 - 존재하지 않는 지원서")
                                                                      .description("존재하지 않는 지원서 ID로 상태 변경 시도 시 400 Bad Request를 반환합니다.")
                                                                      .build())
                            )
                    );
    }
}