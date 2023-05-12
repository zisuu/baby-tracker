package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventService {

    List<Event> listEvents();

    Optional<Event> getEventById(UUID id);
    Event saveNewEvent(Event event);

    void updateEventById(UUID eventId, Event event);

    void deleteById(UUID eventId);

    void patchEventById(UUID eventId, Event event);
}
