package com.side.rest.resume.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;
import com.side.rest.resume.dto.request.ResumeRequestDto;

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
									  .build();
	}

	@Test
	@Transactional
	void create() throws Exception {

		ResumeRequestDto dto = new ResumeRequestDto();
		dto.setContents("""
				안녕하세요. 저는 백엔드 개발자입니다.
				
				- 경력: 5년
				- 주요 기술 스택: Java, Spring Boot, MySQL
				- 프로젝트 경험: 전자상거래 플랫폼 개발
				""");

		this.mockMvc.perform(post("/v1/resume")
								 .accept(MediaType.APPLICATION_JSON)
								 .contentType(MediaType.APPLICATION_JSON)
								 .content(objectMapper.writeValueAsString(dto)))
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
																					 .description("이력서 ID (등록 시 null)")
																					 .optional(),
																  fieldWithPath("contents").type(JsonFieldType.STRING)
																						   .description("이력서 내용")
															  )
															  .build()
							)
						)
					);
	}

}