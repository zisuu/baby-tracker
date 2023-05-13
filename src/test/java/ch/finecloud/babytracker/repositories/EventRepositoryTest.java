package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventType;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    void testEventNoteTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Event savedEvent = eventRepository.save(Event.builder()
                    .notes("TestEveent23482347982748923748923784723894782937482374723847238479TestEvent23482347982748923748923784723894782937482374723847238479TestBaby23482347982748923748923784723894782937482374723847238479TestBaby23482347982748923748923784723894782937482374723847238479").build());

            eventRepository.flush();
        });
    }

    @Test
    void testSaveEvent() {
        Event savedEvent = eventRepository.save(Event.builder()
                .eventType(EventType.CRYING).build());

        eventRepository.flush();
        assertThat(savedEvent).isNotNull();
        assertThat(savedEvent.getId()).isNotNull();
    }

}