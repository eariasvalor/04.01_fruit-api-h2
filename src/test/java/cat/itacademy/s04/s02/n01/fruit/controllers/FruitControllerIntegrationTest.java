package cat.itacademy.s04.s02.n01.fruit.controllers;


import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FruitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /fruits - must create a Fruit and return 201 Created")
    void testCreateFruit_Success() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("Poma", 5);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Poma"))
                .andExpect(jsonPath("$.weightInKilos").value(5));
    }

    @Test
    @DisplayName("POST /fruits - must create 400 Bad Request if the name is empty")
    void testCreateFruit_EmptyName_ReturnsBadRequest() throws Exception {
        // Given
        FruitRequestDTO request = new FruitRequestDTO("", 5);

        // When & Then
        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /fruits - must return 400 Bad Request if the weight is not valid")
    void testCreateFruit_InvalidWeight_ReturnsBadRequest() throws Exception {
        // Given
        FruitRequestDTO request = new FruitRequestDTO("Poma", -1);

        // When & Then
        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /fruits - must return 400 Bad Request if the weight is 0")
    void testCreateFruit_ZeroWeight_ReturnsBadRequest() throws Exception {
        // Given
        FruitRequestDTO request = new FruitRequestDTO("Poma", 0);

        // When & Then
        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}
