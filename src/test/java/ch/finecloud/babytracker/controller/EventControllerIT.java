package ch.finecloud.eventtracker.controller;

import ch.finecloud.babytracker.controller.EventController;
import ch.finecloud.babytracker.controller.NotFoundException;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.mappers.EventMapper;
import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.repositories.EventRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class EventControllerIT {

    @Autowired
    EventController eventController;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventMapper eventMapper;

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

    @Rollback
    @Transactional
    @Test
    void saveNewEventTest() {
        EventDTO eventDTO = EventDTO.builder()
                .notes("New Event")
                .build();
        ResponseEntity responseEntity = eventController.handlePost(eventDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Event event = eventRepository.findById(savedUUID).get();
        assertThat(event).isNotNull();
    }

    @Test
    void testBabyByIdNotFound() {
        assertThrows(NotFoundException.class, () -> eventController.getEventById(UUID.randomUUID()));
    }

    @Test
    void testGetById() {
        Event testEvent = eventRepository.findAll().get(0);
        EventDTO babyDTO = eventController.getEventById(testEvent.getId());
        assertThat(babyDTO).isNotNull();
    }

    @Test
    void testListEvents() {
        List<EventDTO> eventDTOList = eventController.listEvents();
        assertThat(eventDTOList.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        eventRepository.deleteAll();
        List<EventDTO> eventDTOList = eventController.listEvents();
        assertThat(eventDTOList.size()).isEqualTo(0);
    }
}

