package ch.finecloud.babytracker.mappers;

import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventDTO;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {
    Event eventDtoToEvent(EventDTO eventDTO);

    EventDTO eventToEventDto(Event event);
}
