package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.EventDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface EventService {

    Page<EventDTO> listEvents(UUID babyId, Integer pageNumber, Integer pageSize);

    Optional<EventDTO> getEventById(UUID id);
    EventDTO saveNewEvent(EventDTO eventDTO);

    Optional<EventDTO> updateEventById(UUID eventId, EventDTO eventDTO);

    Boolean deleteById(UUID eventId);

    Optional<EventDTO> patchEventById(UUID eventId, EventDTO eventDTO);

    void createAssociation(UUID eventId, UUID babyId);

}
