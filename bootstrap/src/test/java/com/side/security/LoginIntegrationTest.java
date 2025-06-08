package com.side.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testLogin_Success() throws Exception {
		String loginRequest = """
			{
			    "username": "testuser",
			    "password": "password123"
			}
			""";

		mockMvc.perform(post("/api/sign/in")
							.contentType(MediaType.APPLICATION_JSON)
							.content(loginRequest))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.success").value(true))
			   .andExpect(jsonPath("$.accessToken").exists())
			   .andExpect(jsonPath("$.refreshToken").exists());
	}

	@Test
	public void testLogin_Failure() throws Exception {
		String loginRequest = """
			{
			    "username": "wronguser",
			    "password": "wrongpassword"
			}
			""";

		mockMvc.perform(post("/api/sign/in")
							.contentType(MediaType.APPLICATION_JSON)
							.content(loginRequest))
			   .andExpect(status().isUnauthorized())
			   .andExpect(jsonPath("$.success").value(false))
			   .andExpect(jsonPath("$.message").value("로그인에 실패했습니다."));
	}
}
