package ch.finecloud.babytracker.mappers;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.model.BabyDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BabyMapper {
    Baby babyDtoToBaby(BabyDTO babyDTO);

    @AfterMapping
    default void linkEvents(@MappingTarget Baby baby) {
        baby.getEvents().forEach(event -> event.setBaby(baby));
    }

    BabyDTO babyToBabyDto(Baby baby);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Baby partialUpdate(BabyDTO babyDTO, @MappingTarget Baby baby);
}
