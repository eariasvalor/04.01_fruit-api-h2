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
        FruitRequestDTO request = new FruitRequestDTO("", 5);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /fruits - must return 400 Bad Request if the weight is not valid")
    void testCreateFruit_InvalidWeight_ReturnsBadRequest() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("Poma", -1);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /fruits - must return 400 Bad Request if the weight is 0")
    void testCreateFruit_ZeroWeight_ReturnsBadRequest() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("Poma", 0);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /fruits - must return all fruits with 200 OK")
    void testGetAllFruits_Success() throws Exception {
        FruitRequestDTO fruit1 = new FruitRequestDTO("Poma", 5);
        FruitRequestDTO fruit2 = new FruitRequestDTO("Plàtan", 3);
        FruitRequestDTO fruit3 = new FruitRequestDTO("Taronja", 7);

        mockMvc.perform(post("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fruit1)));

        mockMvc.perform(post("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fruit2)));

        mockMvc.perform(post("/fruits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fruit3)));

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Poma"))
                .andExpect(jsonPath("$[0].weightInKilos").value(5))
                .andExpect(jsonPath("$[1].name").value("Plàtan"))
                .andExpect(jsonPath("$[1].weightInKilos").value(3))
                .andExpect(jsonPath("$[2].name").value("Taronja"))
                .andExpect(jsonPath("$[2].weightInKilos").value(7));
    }

    @Test
    @DisplayName("GET /fruits - must return an empty array if there are no fruits.")
    void testGetAllFruits_EmptyList() throws Exception {
        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(jsonPath("$", is(empty())));
    }

}
