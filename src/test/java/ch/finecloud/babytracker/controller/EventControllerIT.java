package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.mappers.EventMapper;
import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.repositories.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EventControllerIT {

    @Autowired
    EventController eventController;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventMapper eventMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    public static final String USERNAME = "userAccount1@example.com";
    public static final String PASSWORD = "password1";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
    void testListEventByEventType() throws Exception {
        mockMvc.perform(get(EventController.BASE_URL)
                        .queryParam("eventType", EventType.FEEDING.name())
                .header("Authorization", "Bearer " + getJwtToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", CoreMatchers.is(5)));
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> eventController.deleteById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        Event testEvent = eventRepository.findAll().get(0);
        ResponseEntity responseEntity = eventController.deleteById(testEvent.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(eventRepository.findById(testEvent.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> eventController.updateById(UUID.randomUUID(), EventDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void updateNotesExistingEvent() {
        Event testEvent = eventRepository.findAll().get(0);
        EventDTO eventDTO = eventMapper.eventToEventDto(testEvent);
        eventDTO.setId(null);
        eventDTO.setVersion(null);
        eventDTO.setNotes("Updated Event");
        ResponseEntity responseEntity = eventController.updateById(testEvent.getId(), eventDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Event updatedEvent = eventRepository.findById(testEvent.getId()).get();
        assertThat(updatedEvent.getNotes()).isEqualTo("Updated Event");
    }

    @Rollback
    @Transactional
    @Test
    void updateEventTypeExistingEvent() {
        Event testEvent = eventRepository.findAll().get(0);
        EventDTO eventDTO = eventMapper.eventToEventDto(testEvent);
        eventDTO.setId(null);
        eventDTO.setVersion(null);
        eventDTO.setEventType(EventType.WAKEUP);
        ResponseEntity responseEntity = eventController.updateById(testEvent.getId(), eventDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Event updatedEvent = eventRepository.findById(testEvent.getId()).get();
        assertThat(updatedEvent.getEventType()).isEqualTo(EventType.WAKEUP);
    }

    @Rollback
    @Transactional
    @Test
    void updateEventDatesExistingEvent() {
        Event testEvent = eventRepository.findAll().get(0);
        EventDTO eventDTO = eventMapper.eventToEventDto(testEvent);
        eventDTO.setId(null);
        eventDTO.setVersion(null);
        eventDTO.setStartDate(LocalDateTime.of(2021, 1, 1, 1, 1, 1));
        eventDTO.setEndDate(LocalDateTime.of(2021, 1, 1, 3, 3, 3));
        ResponseEntity responseEntity = eventController.updateById(testEvent.getId(), eventDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Event updatedEvent = eventRepository.findById(testEvent.getId()).get();
        assertThat(updatedEvent.getStartDate()).isEqualTo(LocalDateTime.of(2021, 1, 1, 1, 1, 1));
        assertThat(updatedEvent.getEndDate()).isEqualTo(LocalDateTime.of(2021, 1, 1, 3, 3, 3));
    }

    @Test
    void testBabyByIdNotFound() {
        assertThrows(NotFoundException.class, () -> eventController.getEventById(String.valueOf(UUID.randomUUID())));
    }

    @Test
    void testGetById() {
        Event testEvent = eventRepository.findAll().get(0);
        EventDTO eventDTO = eventController.getEventById(String.valueOf(testEvent.getId()));
        assertThat(eventDTO).isNotNull();
    }

    @Test
    void testListEvents() {
        Page<EventDTO> eventDTOList = eventController.listEvents(null, 1, 25);
        assertThat(eventDTOList.getContent().size()).isEqualTo(5);
    }
    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        eventRepository.deleteAll();
        Page<EventDTO> eventDTOList = eventController.listEvents(null, 1, 25);
        assertThat(eventDTOList.getContent().size()).isEqualTo(0);
    }
}

