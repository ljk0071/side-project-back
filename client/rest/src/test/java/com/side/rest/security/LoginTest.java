package com.side.rest.security;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SideApplication.class)
@AutoConfigureMockMvc
@DisplayName("로그인 테스트")
@ExtendWith(RestDocumentationExtension.class)
class LoginTest {

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
    void testLogin() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/in")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(Map.of("userId", "system", "password", "gmlwls@9833"))))
                    .andExpect(status().isOk())
                    .andDo(document("로그인", resource(ResourceSnippetParameters.builder()
                                                                             .tag("인증")
                                                                             .summary("사용자 로그인")
                                                                             .description("사용자 ID와 비밀번호로 로그인합니다.")
                                                                             .requestSchema(Schema.schema("로그인 요청"))
                                                                             .requestFields(fieldWithPath("userId").type(JsonFieldType.STRING)
                                                                                                                   .description("사용자 ID"),
                                                                                     fieldWithPath("password").type(JsonFieldType.STRING)
                                                                                                              .description("비밀번호"))
                                                                             .build())));
    }
}
