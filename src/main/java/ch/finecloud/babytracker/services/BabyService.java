package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.Baby;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BabyService {
    List<Baby> listBabys();

    Optional<Baby> getBabyById(UUID id);

    Baby saveNewBaby(Baby baby);

    void updateBabyById(UUID babyId, Baby baby);

    void deleteById(UUID babyId);

    void patchBabyById(UUID babyId, Baby baby);
}
