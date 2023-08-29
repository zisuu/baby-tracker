package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.mappers.EventMapper;
import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
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
    public Page<EventDTO> listEvents(UUID babyId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Event> eventPage;
        if (babyId != null) {
            eventPage = listEventByBabyUuid(babyId, pageRequest);
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

    public Page<Event> listEventByBabyUuid(UUID babyUuid, Pageable pageable) {
        return eventRepository.findAllByBabyId(babyUuid, pageable);
    }

    @Override
    public Optional<EventDTO> getEventById(UUID id) {
        return Optional.ofNullable(eventMapper.eventToEventDto(eventRepository.findById(id).orElse(null)));
    }

    @Override
    public EventDTO saveNewEvent(EventDTO eventDTO) {
        Optional<LocalDateTime> startDate = Optional.ofNullable(eventDTO.getStartDate());
        Optional<LocalDateTime> endDate = Optional.ofNullable(eventDTO.getEndDate());
        if (startDate.isPresent() && endDate.isPresent()) {
            if (startDate.get().isAfter(endDate.get())) {
                throw new IllegalArgumentException("Event start date is after event end date");
            }
        }
        return eventMapper.eventToEventDto(eventRepository.save(eventMapper.eventDtoToEvent(eventDTO)));
    }

    @Override
    public Optional<EventDTO> updateEventById(UUID eventId, EventDTO eventDTO) {
        AtomicReference<Optional<EventDTO>> atomicReference = new AtomicReference<>();
        eventRepository.findById(eventId).ifPresentOrElse(event -> {
            event.setEventType(eventDTO.getEventType());
            event.setNotes(eventDTO.getNotes());
            event.setStartDate(eventDTO.getStartDate());
            event.setEndDate(eventDTO.getEndDate());
            atomicReference.set(Optional.of(eventMapper.eventToEventDto(eventRepository.save(event))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Transactional
    @Override
    public Boolean deleteById(UUID eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            log.debug("EventServiceJPA.deleteById was called with eventId: " + eventId);
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
        LocalDate eventStartDate = event.getStartDate() != null ? event.getStartDate().toLocalDate() : null;
        LocalDate babyBirthday = baby.getBirthday();
        if (eventStartDate != null && eventStartDate.isBefore(babyBirthday)) {
            throw new IllegalArgumentException("Event start date is before baby birthday");
        } else {
            event.setBaby(baby);
            eventRepository.save(event);
        }
    }
}
