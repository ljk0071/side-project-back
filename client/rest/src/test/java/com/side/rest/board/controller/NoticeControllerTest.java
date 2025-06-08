package com.side.rest.board.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.bootstrap.SideApplication;

@SpringBootTest(classes = SideApplication.class)
@AutoConfigureMockMvc
class NoticeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Transactional
	@DisplayName("공지사항_생성_테스트")
	void create() throws Exception {

		// Notice notice = Notice.builder()
		// 					  .article(Article.builder()
		// 									  .title("Test Title")
		// 									  .contents("Test Contents")
		// 									  .build())
		// 					  .build();
		//
		// this.mockMvc.perform(post("/v1/notice")
		// 						 .accept(MediaType.APPLICATION_JSON)
		// 						 .contentType(MediaType.APPLICATION_JSON)
		// 						 .content(objectMapper.writeValueAsString(notice)))
		// 			.andExpect(status().isOk())
		// 			.andDo(print())
		// 			.andReturn();
	}
}
