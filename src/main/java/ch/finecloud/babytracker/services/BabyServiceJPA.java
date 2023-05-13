package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.mappers.BabyMapper;
import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.repositories.BabyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BabyServiceJPA implements BabyService {

    private final BabyRepository babyRepository;
    private final BabyMapper babyMapper;


    @Override
    public List<BabyDTO> listBabys() {
        return babyRepository.findAll().stream()
                .map(babyMapper::babyToBabyDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BabyDTO> getBabyById(UUID id) {
        return Optional.ofNullable(babyMapper.babyToBabyDto(babyRepository.findById(id).orElse(null)));
    }

    @Override
    public BabyDTO saveNewBaby(BabyDTO babyDTO) {
        return babyMapper.babyToBabyDto(babyRepository.save(babyMapper.babyDtoToBaby(babyDTO)));
    }

    @Override
    public Optional<BabyDTO> updateBabyById(UUID babyId, BabyDTO babyDTO) {
        AtomicReference<Optional<BabyDTO>> atomicReference = new AtomicReference<>();
        babyRepository.findById(babyId).ifPresentOrElse(baby -> {
            baby.setName(babyDTO.getName());
            atomicReference.set(Optional.of(babyMapper.babyToBabyDto(babyRepository.save(baby))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID babyId) {
        if (babyRepository.existsById(babyId)) {
            babyRepository.deleteById(babyId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BabyDTO> patchBabyById(UUID babyId, BabyDTO babyDTO) {
        AtomicReference<Optional<BabyDTO>> atomicReference = new AtomicReference<>();

        babyRepository.findById(babyId).ifPresentOrElse(foundBaby -> {
            if (StringUtils.hasText(babyDTO.getName())){
                foundBaby.setName(babyDTO.getName());
            }
            atomicReference.set(Optional.of(babyMapper
                    .babyToBabyDto(babyRepository.save(foundBaby))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
