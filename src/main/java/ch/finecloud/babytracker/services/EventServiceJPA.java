package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.mappers.EventMapper;
import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final BabyRepository babyRepository;
    private final EventMapper eventMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<EventDTO> listEvents(EventType eventType, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Event> eventPage;
        if (eventType != null) {
            eventPage = listEventByType(eventType, pageRequest);
        } else {
            eventPage = eventRepository.findAll(pageRequest);
        }
        return eventPage.map(eventMapper::eventToEventDto);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;
        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }
        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }
        Sort sort = Sort.by(Sort.Order.asc("createdDate"));
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    public Page<Event> listEventByType(EventType eventType, Pageable pageable) {
        return eventRepository.findAllByEventType(eventType, pageable);
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
    public Optional<EventDTO> patchEventById(UUID eventId, EventDTO eventDTO) {
        AtomicReference<Optional<EventDTO>> atomicReference = new AtomicReference<>();
        eventRepository.findById(eventId).ifPresentOrElse(foundEvent -> {
            EventDTO existingEventDTO = eventMapper.eventToEventDto(foundEvent);

            if (StringUtils.hasText(eventDTO.getNotes())) {
                existingEventDTO.setNotes(eventDTO.getNotes());
            }
            atomicReference.set(Optional.of(eventMapper.eventToEventDto(eventRepository.save(foundEvent))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public void createAssociation(UUID eventId, UUID babyId) {
        Event event = eventRepository.findById(eventId).get();
        Baby baby = babyRepository.findById(babyId).get();
        event.setBaby(baby);
        eventRepository.save(event);
    }
}
