package ch.finecloud.babytracker.mappers;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.model.BabyDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BabyMapper {
    Baby babyDtoToBaby(BabyDTO babyDTO);

    BabyDTO babyToBabyDto(Baby baby);
}
