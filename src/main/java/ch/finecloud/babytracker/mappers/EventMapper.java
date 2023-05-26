package ch.finecloud.babytracker.mappers;

import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    Event eventDtoToEvent(EventDTO eventDTO);

    EventDTO eventToEventDto(Event event);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdate(EventDTO eventDTO, @MappingTarget Event event);
}
