package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.services.EventService;
import ch.finecloud.babytracker.services.EventServiceImpl;
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

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EventService eventService;

    @Autowired
    ObjectMapper objectMapper;

    EventServiceImpl eventServiceImpl;

    @Captor
    ArgumentCaptor<EventDTO> eventArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;


    @BeforeEach
    void setUp() {
        eventServiceImpl = new EventServiceImpl();
    }

    @Test
    void testPatchEvent() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents().get(0);
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("notes", "New EventDTO Notes");
        mockMvc.perform(patch("/api/v1/events/" + testEventDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventMap)))
                .andExpect(status().isNoContent());
        verify(eventService).patchEventById(uuidArgumentCaptor.capture(), eventArgumentCaptor.capture());
        assertThat(testEventDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(eventMap.get("notes")).isEqualTo(eventArgumentCaptor.getValue().getNotes());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents().get(0);
        given(eventService.deleteById(any())).willReturn(true);
        mockMvc.perform(delete("/api/v1/events/" + testEventDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(eventService).deleteById(argumentCaptor.capture());
        assertThat(testEventDTO.getId()).isEqualTo(argumentCaptor.getValue());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents().get(0);
        given(eventService.updateEventById(any(UUID.class), any(EventDTO.class))).willReturn(Optional.of(testEventDTO));
        mockMvc.perform(put("/api/v1/events/" + testEventDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEventDTO)))
                .andExpect(status().isNoContent());
        verify(eventService).updateEventById(any(UUID.class), any(EventDTO.class));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents().get(0);
        testEventDTO.setId(null);
        testEventDTO.setVersion(null);
        given(eventService.saveNewEvent(any(EventDTO.class))).willReturn(eventServiceImpl.listEvents().get(1));
        mockMvc.perform(post("/api/v1/events")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEventDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void getEventById() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents().get(0);
        given(eventService.getEventById(testEventDTO.getId())).willReturn(Optional.of(testEventDTO));
        mockMvc.perform(get("/api/v1/events/" + testEventDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEventDTO.getId().toString())))
                .andExpect(jsonPath("$.eventType", is(testEventDTO.getEventType().toString())));
    }

    @Test
    void getEventByIdNotFound() throws Exception {
        given(eventService.getEventById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(EventController.BASE_URL_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}