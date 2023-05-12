package ch.finecloud.babytracker.services;


import ch.finecloud.babytracker.model.Event;
import ch.finecloud.babytracker.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private Map<UUID, Event> eventMap;

    public EventServiceImpl() {
        this.eventMap = new HashMap<>();

        Event event1 = Event.builder()
                .id(UUID.randomUUID())
                .version(1)
                .eventType(EventType.SLEEPING)
                .build();

        Event event2 = Event.builder()
                .id(UUID.randomUUID())
                .version(1)
                .eventType(EventType.DIAPER)
                .build();

        Event event3 = Event.builder()
                .id(UUID.randomUUID())
                .version(1)
                .eventType(EventType.FEEDING)
                .build();

        eventMap.put(event1.getId(), event1);
        eventMap.put(event2.getId(), event2);
        eventMap.put(event3.getId(), event3);
    }
    
    @Override
    public List<Event> listEvents() {
        return new ArrayList<>(eventMap.values());
    }

    @Override
    public Optional<Event> getEventById(UUID id) {
        log.debug("getEventById was called with id: " + id + ", in Service");
        return Optional.of(eventMap.get(id));
    }

    @Override
    public Event saveNewEvent(Event event) {
        Event savedEvent = Event.builder()
                .id(UUID.randomUUID())
                .version(1)
                .eventType(event.getEventType())
                .notes(event.getNotes())
                .endDate(event.getEndDate())
                .build();
        eventMap.put(savedEvent.getId(), savedEvent);
        return savedEvent;
    }

    @Override
    public void updateEventById(UUID eventId, Event event) {
        Event existingEvent = eventMap.get(eventId);
        existingEvent.setEventType(event.getEventType());
        existingEvent.setNotes(event.getNotes());
        existingEvent.setEndDate(event.getEndDate());
        eventMap.put(existingEvent.getId(), existingEvent);
    }

    @Override
    public void deleteById(UUID eventId) {
        eventMap.remove(eventId);
    }

    @Override
    public void patchEventById(UUID eventId, Event event) {
        Event existingBeer = eventMap.get(eventId);

        if(StringUtils.hasText(event.getNotes())) {
            existingBeer.setNotes(event.getNotes());
        }

        if(event.getEndDate() != null) {
            existingBeer.setEndDate(event.getEndDate());
        }

        if(event.getEventType() != null) {
            existingBeer.setEventType(event.getEventType());
        }

    }
}
