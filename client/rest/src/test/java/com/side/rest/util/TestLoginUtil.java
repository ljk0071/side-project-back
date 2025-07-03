package com.side.rest.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestLoginUtil {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    public TestLoginUtil(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public Map<String, String> login() throws Exception {
        return login("system", "gmlwls@9833");
    }

    public Map<String, String> login(String userId, String password) throws Exception {
        MvcResult httpResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/in")
                                                                          .contentType(MediaType.APPLICATION_JSON)
                                                                          .content(objectMapper.writeValueAsString(Map.of("userId", userId, "password", password))))
                                           .andExpect(status().isOk())
                                           .andReturn();

        MockHttpServletResponse response = httpResult.getResponse();

        Map<String, String> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});

        result.put("accessToken", Objects.requireNonNull(response.getCookie("Authorization")).getValue());

        return result;
    }
}
