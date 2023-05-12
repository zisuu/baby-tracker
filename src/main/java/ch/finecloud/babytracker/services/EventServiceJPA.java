package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.mappers.EventMapper;
import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class EventServiceJPA implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;


    @Override
    public List<EventDTO> listEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::eventToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventDTO> getEventById(UUID id) {
        return Optional.ofNullable(eventMapper.eventToEventDto(eventRepository.findById(id).orElse(null)));
    }

    @Override
    public EventDTO saveNewEvent(EventDTO eventDTO) {
        return eventMapper.eventToEventDto(eventRepository.save(eventMapper.eventDtoToEvent(eventDTO)));
    }

    @Override
    public Optional<EventDTO> updateEventById(UUID eventId, EventDTO eventDTO) {
        AtomicReference<Optional<EventDTO>> atomicReference = new AtomicReference<>();
        eventRepository.findById(eventId).ifPresentOrElse(event -> {
            event.setNotes(eventDTO.getNotes());
            atomicReference.set(Optional.of(eventMapper.eventToEventDto(eventRepository.save(event))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            return true;
        }
        return false;
    }

    @Override
    public void patchEventById(UUID eventId, EventDTO eventDTO) {
        AtomicReference<Optional<EventDTO>> atomicReference = new AtomicReference<>();
        eventRepository.findById(eventId).ifPresentOrElse(foundEvent -> {
            EventDTO existingEventDTO = eventMapper.eventToEventDto(foundEvent);

        if(StringUtils.hasText(eventDTO.getNotes())) {
            existingEventDTO.setNotes(eventDTO.getNotes());
        }
            atomicReference.set(Optional.of(eventMapper.eventToEventDto(eventRepository.save(foundEvent))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
    }
}
