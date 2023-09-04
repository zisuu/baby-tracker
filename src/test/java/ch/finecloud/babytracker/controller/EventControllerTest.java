package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.services.EventService;
import ch.finecloud.babytracker.services.EventServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

@SpringBootTest
class EventControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    EventService eventService;

    @Autowired
    ObjectMapper objectMapper;

    EventServiceImpl eventServiceImpl;

    @Captor
    ArgumentCaptor<EventDTO> eventArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    public static final String USERNAME = "userAccount1@example.com";
    public static final String PASSWORD = "password1";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        eventServiceImpl = new EventServiceImpl();
    }

    private String getJwtToken() throws Exception {
        String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", USERNAME, PASSWORD);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return contentAsString.substring(8, contentAsString.length() - 2);
    }

    @Test
    void testCreateEventNullEventName() throws Exception {
        EventDTO eventDTO = EventDTO.builder().build();
        given(eventService.saveNewEvent(any(EventDTO.class))).willReturn(eventServiceImpl.listEvents(null, 1, 25).getContent().get(1));
        MvcResult MvcResult = mockMvc.perform(post(EventController.BASE_URL)
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateEventBlankType() throws Exception {
        EventDTO eventDTO = eventServiceImpl.listEvents(null, 1, 25).getContent().get(0);
        eventDTO.setEventType(null);
        eventDTO.setNotes("New EventDTO Notes");
        given(eventService.updateEventById(any(UUID.class), any(EventDTO.class))).willReturn(Optional.of(eventDTO));
        MvcResult MvcResult = mockMvc.perform(put(EventController.BASE_URL_ID, eventDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPatchEvent() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents(null, 1, 25).getContent().get(0);
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("notes", "New EventDTO Notes");
        mockMvc.perform(patch("/api/v1/events/" + testEventDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventMap)))
                .andExpect(status().isNoContent());
        verify(eventService).patchEventById(uuidArgumentCaptor.capture(), eventArgumentCaptor.capture());
        assertThat(testEventDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(eventMap.get("notes")).isEqualTo(eventArgumentCaptor.getValue().getNotes());
    }

    @Test
    void testDeleteEvent() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents(null, 1, 25).getContent().get(0);
        given(eventService.deleteById(any())).willReturn(true);
        mockMvc.perform(delete("/api/v1/events/" + testEventDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(eventService).deleteById(argumentCaptor.capture());
        assertThat(testEventDTO.getId()).isEqualTo(argumentCaptor.getValue());
    }

    @Test
    void testUpdateEvent() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents(null, 1, 25).getContent().get(0);
        given(eventService.updateEventById(any(UUID.class), any(EventDTO.class))).willReturn(Optional.of(testEventDTO));
        mockMvc.perform(put("/api/v1/events/" + testEventDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEventDTO)))
                .andExpect(status().isNoContent());
        verify(eventService).updateEventById(any(UUID.class), any(EventDTO.class));
    }

    @Test
    void testCreateNewEvent() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents(null, 1, 25).getContent().get(0);
        testEventDTO.setId(null);
        testEventDTO.setVersion(null);
        given(eventService.saveNewEvent(any(EventDTO.class))).willReturn(eventServiceImpl.listEvents(null, 1, 25).getContent().get(1));
        mockMvc.perform(post("/api/v1/events")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEventDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testListEvents() throws Exception {
        given(eventService.listEvents(any(), any(), any())).willReturn(eventServiceImpl.listEvents(null, null, null));
        mockMvc.perform(get("/api/v1/events")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", Is.is(3)));
    }

    @Test
    void getEventById() throws Exception {
        EventDTO testEventDTO = eventServiceImpl.listEvents(null, 1, 25).getContent().get(0);
        given(eventService.getEventById(testEventDTO.getId())).willReturn(Optional.of(testEventDTO));
        mockMvc.perform(get("/api/v1/events/" + testEventDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
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
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}