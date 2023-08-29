package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class EventServiceJPATest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private BabyRepository babyRepository;

    @InjectMocks
    private EventServiceJPA eventServiceJPA;

    @Test
    public void testCreateAssociation_WithValidDate() {
        UUID eventId = UUID.randomUUID();
        UUID babyId = UUID.randomUUID();
        LocalDate babyBirthday = LocalDate.of(2020, 1, 1);
        LocalDate eventStartDate = LocalDate.of(2022, 1, 1);

        Event event = new Event();
        event.setStartDate(eventStartDate.atStartOfDay());

        Baby baby = new Baby();
        baby.setBirthday(babyBirthday);

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Mockito.when(babyRepository.findById(babyId)).thenReturn(Optional.of(baby));

        eventServiceJPA.createAssociation(eventId, babyId);

        Mockito.verify(eventRepository, Mockito.times(1)).save(event);
        assertEquals(baby, event.getBaby());
    }

    @Test
    public void testCreateAssociation_WithInvalidDate() {
        UUID eventId = UUID.randomUUID();
        UUID babyId = UUID.randomUUID();
        LocalDate babyBirthday = LocalDate.of(2020, 1, 1);
        LocalDate eventStartDate = LocalDate.of(2019, 1, 1);

        Event event = new Event();
        event.setStartDate(eventStartDate.atStartOfDay());

        Baby baby = new Baby();
        baby.setBirthday(babyBirthday);

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Mockito.when(babyRepository.findById(babyId)).thenReturn(Optional.of(baby));

        assertThrows(IllegalArgumentException.class, () -> eventServiceJPA.createAssociation(eventId, babyId));
    }

    @Test
    void testCreateNewEventEndDateAfterStartDate() {
        EventDTO event = EventDTO.builder()
                .eventType(EventType.SLEEPING)
                .startDate(LocalDateTime.of(2020, 1, 1, 1, 1))
                .endDate(LocalDateTime.of(2020, 1, 1, 1, 0))
                .build();

        assertThrows(IllegalArgumentException.class, () -> eventServiceJPA.saveNewEvent(event));
    }
}

