package ch.finecloud.babytracker.services;


import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private Map<UUID, EventDTO> eventMap;

    public EventServiceImpl() {
        this.eventMap = new HashMap<>();

        EventDTO eventDTO1 = EventDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .eventType(EventType.SLEEPING)
                .build();

        EventDTO eventDTO2 = EventDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .eventType(EventType.DIAPER)
                .build();

        EventDTO eventDTO3 = EventDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .eventType(EventType.FEEDING)
                .build();

        eventMap.put(eventDTO1.getId(), eventDTO1);
        eventMap.put(eventDTO2.getId(), eventDTO2);
        eventMap.put(eventDTO3.getId(), eventDTO3);
    }
    
    @Override
    public List<EventDTO> listEvents() {
        return new ArrayList<>(eventMap.values());
    }

    @Override
    public Optional<EventDTO> getEventById(UUID id) {
        log.debug("getEventById was called with id: " + id + ", in Service");
        return Optional.of(eventMap.get(id));
    }

    @Override
    public EventDTO saveNewEvent(EventDTO eventDTO) {
        EventDTO savedEventDTO = EventDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .eventType(eventDTO.getEventType())
                .notes(eventDTO.getNotes())
                .endDate(eventDTO.getEndDate())
                .build();
        eventMap.put(savedEventDTO.getId(), savedEventDTO);
        return savedEventDTO;
    }

    @Override
    public void updateEventById(UUID eventId, EventDTO eventDTO) {
        EventDTO existingEventDTO = eventMap.get(eventId);
        existingEventDTO.setEventType(eventDTO.getEventType());
        existingEventDTO.setNotes(eventDTO.getNotes());
        existingEventDTO.setEndDate(eventDTO.getEndDate());
        eventMap.put(existingEventDTO.getId(), existingEventDTO);
    }

    @Override
    public void deleteById(UUID eventId) {
        eventMap.remove(eventId);
    }

    @Override
    public void patchEventById(UUID eventId, EventDTO eventDTO) {
        EventDTO existingBeer = eventMap.get(eventId);

        if(StringUtils.hasText(eventDTO.getNotes())) {
            existingBeer.setNotes(eventDTO.getNotes());
        }

        if(eventDTO.getEndDate() != null) {
            existingBeer.setEndDate(eventDTO.getEndDate());
        }

        if(eventDTO.getEventType() != null) {
            existingBeer.setEventType(eventDTO.getEventType());
        }

    }
}
