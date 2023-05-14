package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.model.EventType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventService {

    List<EventDTO> listEvents(EventType eventType);

    Optional<EventDTO> getEventById(UUID id);
    EventDTO saveNewEvent(EventDTO eventDTO);

    Optional<EventDTO> updateEventById(UUID eventId, EventDTO eventDTO);

    Boolean deleteById(UUID eventId);

    Optional<EventDTO> patchEventById(UUID eventId, EventDTO eventDTO);
}
