package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.Event;
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
import static org.junit.jupiter.api.Assertions.*;
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
    ArgumentCaptor<Event> eventArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;


    @BeforeEach
    void setUp() {
        eventServiceImpl = new EventServiceImpl();
    }

    @Test
    void testPatchEvent() throws Exception {
        Event testEvent = eventServiceImpl.listEvents().get(0);
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("notes", "New Event Notes");
        mockMvc.perform(patch("/api/v1/events/" + testEvent.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventMap)))
                .andExpect(status().isNoContent());
        verify(eventService).patchEventById(uuidArgumentCaptor.capture(), eventArgumentCaptor.capture());
        assertThat(testEvent.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(eventMap.get("notes")).isEqualTo(eventArgumentCaptor.getValue().getNotes());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Event testEvent = eventServiceImpl.listEvents().get(0);
        mockMvc.perform(delete("/api/v1/events/" + testEvent.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(eventService).deleteById(argumentCaptor.capture());
        assertThat(testEvent.getId()).isEqualTo(argumentCaptor.getValue());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Event testEvent = eventServiceImpl.listEvents().get(0);
        mockMvc.perform(put("/api/v1/events/" + testEvent.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEvent)))
                .andExpect(status().isNoContent());
        verify(eventService).updateEventById(any(UUID.class), any(Event.class));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        Event testEvent = eventServiceImpl.listEvents().get(0);
        testEvent.setId(null);
        testEvent.setVersion(null);
        given(eventService.saveNewEvent(any(Event.class))).willReturn(eventServiceImpl.listEvents().get(1));
        mockMvc.perform(post("/api/v1/events")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEvent)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void getEventById() throws Exception {
        Event testEvent = eventServiceImpl.listEvents().get(0);
        given(eventService.getEventById(testEvent.getId())).willReturn(Optional.of(testEvent));
        mockMvc.perform(get("/api/v1/events/" + testEvent.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEvent.getId().toString())))
                .andExpect(jsonPath("$.eventType", is(testEvent.getEventType().toString())));
    }

    @Test
    void getEventByIdNotFound() throws Exception {
        given(eventService.getEventById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(EventController.BASE_URL_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}