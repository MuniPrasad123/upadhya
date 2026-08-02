package com.upadhya.common.api;

import com.upadhya.common.response.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
class HealthControllerTest {
    @Autowired MockMvc mockMvc;

    @Test
    void returnsApplicationHealth() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application").value("Upadhya"))
                .andExpect(jsonPath("$.tagline").value("AI Learning Mentor"))
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
