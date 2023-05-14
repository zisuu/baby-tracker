package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.services.BabyService;
import ch.finecloud.babytracker.services.BabyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    void testCreateBabyNullName() throws Exception {
        BabyDTO babyDTO = BabyDTO.builder().build();
        given(babyService.saveNewBaby(any(BabyDTO.class))).willReturn(babyServiceImpl.listBabys(null, 1, 25).getContent().get(1));
        MvcResult MvcResult = mockMvc.perform(post(BabyController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(babyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();
        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateBabyBlankName() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys(null, 1, 25).getContent().get(0);
        testBabyDTO.setName("");
        given(babyService.updateBabyById(any(UUID.class), any(BabyDTO.class))).willReturn(Optional.of(testBabyDTO));
        MvcResult MvcResult = mockMvc.perform(put(BabyController.BASE_URL_ID, testBabyDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBabyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPatchBaby() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys(null, 1, 25).getContent().get(0);
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
    void testDeleteBaby() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys(null, 1, 25).getContent().get(0);
        given(babyService.deleteById(any())).willReturn(true);
        mockMvc.perform(delete("/api/v1/babys/" + testBabyDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(babyService).deleteById(uuidArgumentCaptor.capture());
        assertThat(testBabyDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBaby() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys(null, 1, 25).getContent().get(0);
        given(babyService.updateBabyById(any(UUID.class), any(BabyDTO.class))).willReturn(Optional.of(testBabyDTO));
        mockMvc.perform(put("/api/v1/babys/" + testBabyDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBabyDTO)))
                .andExpect(status().isNoContent());
        verify(babyService).updateBabyById(any(UUID.class), any(BabyDTO.class));
    }

    @Test
    void testCreateNewBaby() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys(null, 1, 25).getContent().get(0);
        testBabyDTO.setId(null);
        testBabyDTO.setVersion(null);
        given(babyService.saveNewBaby(any(BabyDTO.class))).willReturn(babyServiceImpl.listBabys(null, 1, 25).getContent().get(1));
        mockMvc.perform(post("/api/v1/babys")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBabyDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testListBabys() throws Exception {
        given(babyService.listBabys(any(), any(), any())).willReturn(babyServiceImpl.listBabys(null, null, null));
        mockMvc.perform(get("/api/v1/babys")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", Is.is(3)));
    }

    @Test
    void getBabyById() throws Exception {
        BabyDTO testBabyDTO = babyServiceImpl.listBabys(null, 1, 25).getContent().get(0);
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