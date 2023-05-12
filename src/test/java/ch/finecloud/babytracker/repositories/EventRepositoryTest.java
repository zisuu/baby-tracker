package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventType;
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
    void testSaveEvent() {
        Event savedEvent = eventRepository.save(Event.builder()
                .eventType(EventType.CRYING).build());
        assertThat(savedEvent).isNotNull();
        assertThat(savedEvent.getId()).isNotNull();
    }

}