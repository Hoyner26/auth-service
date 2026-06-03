package com.explorecr.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestPropertySource(properties = {
    "app.admin.password=Admin1234!"
})
class AuthFlowTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registersLogsInAndReadsCurrentUser() throws Exception {
        String registerBody = """
            {
              "name": "Juan Costa",
              "email": "juan@explorecr.com",
              "password": "MiPassword123!",
              "confirmPassword": "MiPassword123!"
            }
            """;

        String registerResponse = mockMvc.perform(post("/auth/register")
                .contentType("application/json")
                .content(registerBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.token").isString())
            .andExpect(jsonPath("$.data.user.email").value("juan@explorecr.com"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        String token = objectMapper.readTree(registerResponse).path("data").path("token").asText();

        mockMvc.perform(get("/auth/me").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.role").value("user"));
    }

    @Test
    void adminCanListUsers() throws Exception {
        String loginResponse = mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content("""
                    {
                      "email": "admin@explorecr.com",
                      "password": "Admin1234!"
                    }
                    """))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode data = objectMapper.readTree(loginResponse).path("data");
        String token = data.path("token").asText();

        mockMvc.perform(get("/users").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
    }
}
