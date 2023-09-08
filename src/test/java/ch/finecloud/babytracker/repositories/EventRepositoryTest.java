package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.bootstrap.BootstrapData;
import ch.finecloud.babytracker.config.TestConfig;
import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.services.BabyCsvServiceImpl;
import ch.finecloud.babytracker.services.EventCsvServiceImpl;
import ch.finecloud.babytracker.services.UserCsvServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({BootstrapData.class, UserCsvServiceImpl.class, EventCsvServiceImpl.class, BabyCsvServiceImpl.class, TestConfig.class})
class EventRepositoryTest {

    @Autowired
    BabyRepository babyRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserAccountRepository userAccountRepository;


    Baby testBaby;

    @BeforeEach
    void setUp() {
        testBaby = babyRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testAddBabyAddEvent() {
        Event event = Event.builder()
                .eventType(EventType.SLEEPING)
                .build();
        event.setBaby(testBaby);
        Event savedEvent = eventRepository.save(event);
        eventRepository.flush();

        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());

        Event fetchedEvent = eventRepository.getById(savedEvent.getId());

        assertNotNull(fetchedEvent);
        assertNotNull(fetchedEvent.getId());
        assertNotNull(fetchedEvent.getBaby());
    }

    @Transactional
    @Test
    void testAddBabyAddEventDeleteEvent() {
        Event event = Event.builder()
                .eventType(EventType.SLEEPING)
                .build();
        event.setBaby(testBaby);
        Event savedEvent = eventRepository.save(event);
        eventRepository.flush();

        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());

        Event fetchedEvent = eventRepository.getById(savedEvent.getId());

        assertNotNull(fetchedEvent);
        assertNotNull(fetchedEvent.getId());
        assertNotNull(fetchedEvent.getBaby());

        eventRepository.delete(fetchedEvent);
        eventRepository.flush();
        assertThat(babyRepository.findById(testBaby.getId()).get().getEvents()).isEmpty();
    }

}