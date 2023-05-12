package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.BabyDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BabyService {
    List<BabyDTO> listBabys();

    Optional<BabyDTO> getBabyById(UUID id);

    BabyDTO saveNewBaby(BabyDTO babyDTO);

    void updateBabyById(UUID babyId, BabyDTO babyDTO);

    void deleteById(UUID babyId);

    void patchBabyById(UUID babyId, BabyDTO babyDTO);
}
