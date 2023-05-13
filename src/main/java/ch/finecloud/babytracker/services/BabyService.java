package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.BabyDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BabyService {
    List<BabyDTO> listBabys();

    Optional<BabyDTO> getBabyById(UUID id);

    BabyDTO saveNewBaby(BabyDTO babyDTO);

    Optional<BabyDTO> updateBabyById(UUID babyId, BabyDTO babyDTO);

    Boolean deleteById(UUID babyId);

    Optional<BabyDTO> patchBabyById(UUID babyId, BabyDTO babyDTO);
}
