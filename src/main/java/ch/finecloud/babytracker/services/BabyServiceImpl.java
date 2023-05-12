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
    public List<BabyDTO> listBabys() {
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
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .name(babyDTO.getName())
                .build();

        babyMap.put(savedBabyDTO.getId(), savedBabyDTO);
        return savedBabyDTO;
    }

    @Override
    public void updateBabyById(UUID beerId, BabyDTO babyDTO) {
        BabyDTO existingBabyDTO = babyMap.get(beerId);
        existingBabyDTO.setName(babyDTO.getName());
        babyMap.put(existingBabyDTO.getId(), existingBabyDTO);
    }

    @Override
    public void deleteById(UUID babyId) {
        babyMap.remove(babyId);
    }

    @Override
    public void patchBabyById(UUID babyId, BabyDTO babyDTO) {
        BabyDTO existingBabyDTO = babyMap.get(babyId);

        if(StringUtils.hasText(babyDTO.getName())) {
            existingBabyDTO.setName(babyDTO.getName());
        }
    }
}