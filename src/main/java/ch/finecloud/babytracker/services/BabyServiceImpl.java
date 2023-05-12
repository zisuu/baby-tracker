package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.Baby;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BabyServiceImpl implements BabyService {


    private Map<UUID, Baby> babyMap;

    public BabyServiceImpl() {
        this.babyMap = new HashMap<>();

        Baby baby1 = Baby.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name("Hans Fischer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Baby baby2 = Baby.builder()
                .id(UUID.randomUUID())
                .version(2)
                .name("Peter Muster")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Baby baby3 = Baby.builder()
                .id(UUID.randomUUID())
                .version(3)
                .name("Max Müller")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        babyMap.put(baby1.getId(), baby1);
        babyMap.put(baby2.getId(), baby2);
        babyMap.put(baby3.getId(), baby3);
    }

    @Override
    public List<Baby> listBabys() {
        return new ArrayList<>(babyMap.values());
    }

    @Override
    public Optional<Baby> getBabyById(UUID id) {
        log.debug("getBabyById was called with id: " + id + ", in Service");
        return Optional.of(babyMap.get(id));
    }

    @Override
    public Baby saveNewBaby(Baby baby) {
        Baby savedBaby = Baby.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .name(baby.getName())
                .build();

        babyMap.put(savedBaby.getId(), savedBaby);
        return savedBaby;
    }

    @Override
    public void updateBabyById(UUID beerId, Baby baby) {
        Baby existingBaby = babyMap.get(beerId);
        existingBaby.setName(baby.getName());
        babyMap.put(existingBaby.getId(), existingBaby);
    }

    @Override
    public void deleteById(UUID babyId) {
        babyMap.remove(babyId);
    }

    @Override
    public void patchBabyById(UUID babyId, Baby baby) {
        Baby existingBaby = babyMap.get(babyId);

        if(StringUtils.hasText(baby.getName())) {
            existingBaby.setName(baby.getName());
        }
    }
}
