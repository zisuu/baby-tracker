package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.BabyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BabyServiceImpl implements BabyService {


    private Map<UUID, BabyDTO> babyMap;

    public BabyServiceImpl() {
        this.babyMap = new HashMap<>();

        BabyDTO babyDTO1 = BabyDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name("Hans Fischer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        BabyDTO babyDTO2 = BabyDTO.builder()
                .id(UUID.randomUUID())
                .version(2)
                .name("Peter Muster")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        BabyDTO babyDTO3 = BabyDTO.builder()
                .id(UUID.randomUUID())
                .version(3)
                .name("Max Müller")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        babyMap.put(babyDTO1.getId(), babyDTO1);
        babyMap.put(babyDTO2.getId(), babyDTO2);
        babyMap.put(babyDTO3.getId(), babyDTO3);
    }

    @Override
    public List<BabyDTO> listBabys(String name) {
        return new ArrayList<>(babyMap.values());
    }

    @Override
    public Optional<BabyDTO> getBabyById(UUID id) {
        log.debug("getBabyById was called with id: " + id + ", in Service");
        return Optional.of(babyMap.get(id));
    }

    @Override
    public BabyDTO saveNewBaby(BabyDTO babyDTO) {
        BabyDTO savedBabyDTO = BabyDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .name(babyDTO.getName())
                .build();

        babyMap.put(savedBabyDTO.getId(), savedBabyDTO);
        return savedBabyDTO;
    }

    @Override
    public Optional<BabyDTO> updateBabyById(UUID babyId, BabyDTO baby) {
        BabyDTO existing = babyMap.get(babyId);
        existing.setName(baby.getName());
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteById(UUID babyId) {
        babyMap.remove(babyId);
        return true;
    }


    @Override
    public Optional<BabyDTO> patchBabyById(UUID babyId, BabyDTO baby) {
        BabyDTO existing = babyMap.get(babyId);

        if (StringUtils.hasText(baby.getName())) {
            existing.setName(baby.getName());
        }

        return Optional.of(existing);
    }

}
