package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.EventDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventService {

    List<EventDTO> listEvents();

    Optional<EventDTO> getEventById(UUID id);
    EventDTO saveNewEvent(EventDTO eventDTO);

    void updateEventById(UUID eventId, EventDTO eventDTO);

    void deleteById(UUID eventId);

    void patchEventById(UUID eventId, EventDTO eventDTO);
}
