package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.services.BabyService;
import ch.finecloud.babytracker.services.BabyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(BabyController.class)
class BabyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BabyService babyService;

    @Autowired
    ObjectMapper objectMapper;

    BabyServiceImpl babyServiceImpl;

    @Captor
    ArgumentCaptor<BabyDTO> babyArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @BeforeEach
    void setUp() {
        babyServiceImpl = new BabyServiceImpl();
    }

    @Test
    void testPatchBeer() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys().get(0);
        Map<String, Object> babyMap = new HashMap<>();
        babyMap.put("name", "New BabyDTO Name");
        mockMvc.perform(patch("/api/v1/babys/" + testBabyDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(babyMap)))
                .andExpect(status().isNoContent());
        verify(babyService).patchBabyById(uuidArgumentCaptor.capture(), babyArgumentCaptor.capture());
        assertThat(testBabyDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(babyMap.get("name")).isEqualTo(babyArgumentCaptor.getValue().getName());
    }

    @Test
    void testDeleteBeer() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys().get(0);
        mockMvc.perform(delete("/api/v1/babys/" + testBabyDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(babyService).deleteById(uuidArgumentCaptor.capture());
        assertThat(testBabyDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeer() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys().get(0);
        mockMvc.perform(put("/api/v1/babys/" + testBabyDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBabyDTO)))
                .andExpect(status().isNoContent());
        verify(babyService).updateBabyById(any(UUID.class), any(BabyDTO.class));
    }

    @Test
    void testCreateNewBaby() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys().get(0);
        testBabyDTO.setId(null);
        testBabyDTO.setVersion(null);
        given(babyService.saveNewBaby(any(BabyDTO.class))).willReturn(babyServiceImpl.listBabys().get(1));
        mockMvc.perform(post("/api/v1/babys")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBabyDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testListBabys() throws Exception {
        given(babyService.listBabys()).willReturn(babyServiceImpl.listBabys());
        mockMvc.perform(get("/api/v1/babys")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getBabyById() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys().get(0);
        given(babyService.getBabyById(testBabyDTO.getId())).willReturn(Optional.of(testBabyDTO));
        mockMvc.perform(get("/api/v1/babys/" + testBabyDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBabyDTO.getId().toString())))
                .andExpect(jsonPath("$.name", is(testBabyDTO.getName().toString())));
    }

    @Test
    void getBabyByIdNotFound() throws Exception {
        given(babyService.getBabyById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(EventController.BASE_URL_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}