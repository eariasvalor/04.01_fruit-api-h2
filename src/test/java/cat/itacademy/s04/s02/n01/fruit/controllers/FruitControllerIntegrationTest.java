package cat.itacademy.s04.s02.n01.fruit.controllers;


import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitResponseDTO;
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
        FruitRequestDTO request = new FruitRequestDTO("Apple", 5);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Apple"))
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
        FruitRequestDTO request = new FruitRequestDTO("Apple", -1);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /fruits - must return 400 Bad Request if the weight is 0")
    void testCreateFruit_ZeroWeight_ReturnsBadRequest() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("Apple", 0);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /fruits - must return all fruits with 200 OK")
    void testGetAllFruits_Success() throws Exception {
        FruitRequestDTO fruit1 = new FruitRequestDTO("Apple", 5);
        FruitRequestDTO fruit2 = new FruitRequestDTO("Banana", 3);
        FruitRequestDTO fruit3 = new FruitRequestDTO("Orange", 7);

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
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[0].weightInKilos").value(5))
                .andExpect(jsonPath("$[1].name").value("Banana"))
                .andExpect(jsonPath("$[1].weightInKilos").value(3))
                .andExpect(jsonPath("$[2].name").value("Orange"))
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

    @Test
    @DisplayName("GET /fruits/{id} - must return a fruit with 200 OK")
    void testGetFruitById_Success() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO("Apple", 5);

        String response = mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        FruitResponseDTO createdFruit = objectMapper.readValue(
                response, FruitResponseDTO.class);
        Long fruitId = createdFruit.getId();

        mockMvc.perform(get("/fruits/" + fruitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fruitId))
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(5));
    }

    @Test
    @DisplayName("GET /fruits/{id} - must return 404 Not Found if it doesn't exist")
    void testGetFruitById_NotFound() throws Exception {
                Long nonExistentId = 999L;

        mockMvc.perform(get("/fruits/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(
                        containsString("Fruit not found")));
    }

    @Test
    @DisplayName("PUT /fruits/{id} - must update and return 200 OK")
    void testUpdateFruit_Success() throws Exception {
        FruitRequestDTO createRequest = new FruitRequestDTO("Apple", 5);

        String createResponse = mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        FruitResponseDTO createdFruit = objectMapper.readValue(
                createResponse, FruitResponseDTO.class);
        Long fruitId = createdFruit.getId();

        FruitRequestDTO updateRequest = new FruitRequestDTO("Apple updated", 10);

        mockMvc.perform(put("/fruits/" + fruitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fruitId))
                .andExpect(jsonPath("$.name").value("Apple updated"))
                .andExpect(jsonPath("$.weightInKilos").value(10));

        mockMvc.perform(get("/fruits/" + fruitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Poma Actualitzada"))
                .andExpect(jsonPath("$.weightInKilos").value(10));
    }

    @Test
    @DisplayName("PUT /fruits/{id} - must return 404 if it doesn't exist")
    void testUpdateFruit_NotFound() throws Exception {
        Long nonExistentId = 999L;
        FruitRequestDTO request = new FruitRequestDTO("Apple", 5);

        mockMvc.perform(put("/fruits/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        containsString("Fruit not found")));
    }

    @Test
    @DisplayName("PUT /fruits/{id} - must return 400 if data is not valid")
    void testUpdateFruit_InvalidData_ReturnsBadRequest() throws Exception {

        FruitRequestDTO createRequest = new FruitRequestDTO("Apple", 5);

        String createResponse = mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        FruitResponseDTO createdFruit = objectMapper.readValue(
                createResponse, FruitResponseDTO.class);
        Long fruitId = createdFruit.getId();

        FruitRequestDTO invalidRequest = new FruitRequestDTO("", -5);

        mockMvc.perform(put("/fruits/" + fruitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}
