package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.mappers.BabyMapper;
import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.repositories.BabyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BabyDTO> listBabys(String name, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Baby> beerPage;
        if (StringUtils.hasText(name)) {
            beerPage = listBabyByName(name, pageRequest);
        } else {
            beerPage = babyRepository.findAll(pageRequest);
        }
        return beerPage.map(babyMapper::babyToBabyDto);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;
        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }
        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }
        return PageRequest.of(queryPageNumber, queryPageSize);
    }

    public Page<Baby> listBabyByName(String name, Pageable pageable) {
        return babyRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%", pageable);
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
