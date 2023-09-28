package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.BabyDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BabyService {
    Page<BabyDTO> listBabiesByUserAccountEmail(String userAccountEmail, String babyName, Integer pageNumber, Integer pageSize);

    Optional<BabyDTO> getBabyById(UUID id);

    BabyDTO saveNewBaby(BabyDTO babyDTO);

    Optional<BabyDTO> updateBabyById(UUID babyId, BabyDTO babyDTO);

    Boolean deleteById(UUID babyId);

    Optional<BabyDTO> patchBabyById(UUID babyId, BabyDTO babyDTO);

    void createAssociation(UUID babyId, UUID userId);
}
