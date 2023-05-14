package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.bootstrap.BootstrapData;
import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.services.BabyCsvServiceImpl;
import ch.finecloud.babytracker.services.EventCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, EventCsvServiceImpl.class, BabyCsvServiceImpl.class})
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
    void testGetEventByType() {
        List<Event> list = eventRepository.findAllByEventType(EventType.DIAPER);
        assertThat(list.size()).isEqualTo(1);
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