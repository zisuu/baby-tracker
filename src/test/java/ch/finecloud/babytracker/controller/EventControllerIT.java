package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.mappers.EventMapper;
import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.repositories.BabyRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testListEventByEventType() throws Exception {
        mockMvc.perform(get(EventController.BASE_URL).queryParam("eventType", EventType.FEEDING.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", CoreMatchers.is(1)));
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
    void updateExistingEvent() {
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

//    @Rollback
//    @Transactional
//    @Test
//    void saveNewEventTest() {
//        EventDTO eventDTO = EventDTO.builder()
//                .notes("New Event")
//                .build();
//        ResponseEntity responseEntity = eventController.handlePost(eventDTO);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
//        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
//        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
//        UUID savedUUID = UUID.fromString(locationUUID[4]);
//        Event event = eventRepository.findById(savedUUID).get();
//        assertThat(event).isNotNull();
//    }

    @Test
    void testBabyByIdNotFound() {
        assertThrows(NotFoundException.class, () -> eventController.getEventById(UUID.randomUUID()));
    }

    @Test
    void testGetById() {
        Event testEvent = eventRepository.findAll().get(0);
        EventDTO eventDTO = eventController.getEventById(testEvent.getId());
        assertThat(eventDTO).isNotNull();
    }

    @Test
    void testListEvents() {
        Page<EventDTO> eventDTOList = eventController.listEvents(null, 1, 25);
        assertThat(eventDTOList.getContent().size()).isEqualTo(3);
    }
    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        eventRepository.deleteAll();
        Page<EventDTO> eventDTOList = eventController.listEvents(null, 1, 25);
        assertThat(eventDTOList.getContent().size()).isEqualTo(3);
    }
}

