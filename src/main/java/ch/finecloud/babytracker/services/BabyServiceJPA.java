package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.mappers.BabyMapper;
import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class BabyServiceJPA implements BabyService {

    private final BabyRepository babyRepository;
    private final UserAccountRepository userAccountRepository;
    private final BabyMapper babyMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BabyDTO> listBabies(String name, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Baby> babyPage;
        if (StringUtils.hasText(name)) {
            babyPage = listBabyByName(name, pageRequest);
        } else {
            babyPage = babyRepository.findAll(pageRequest);
        }
        return babyPage.map(babyMapper::babyToBabyDto);
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
        Sort sort = Sort.by(Sort.Order.asc("name"));
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
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
        }, () -> atomicReference.set(Optional.empty()));
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
            if (StringUtils.hasText(babyDTO.getName())) {
                foundBaby.setName(babyDTO.getName());
            }
            atomicReference.set(Optional.of(babyMapper
                    .babyToBabyDto(babyRepository.save(foundBaby))));
        }, () -> atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }


    @Override
    public void createAssociation(UUID babyId, UUID userId) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new EntityNotFoundException("Baby not found with ID: " + babyId));

        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        baby.setUserAccount(user);
        babyRepository.save(baby);
    }
}
