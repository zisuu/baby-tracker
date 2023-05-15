package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.mappers.UserAccountMapper;
import ch.finecloud.babytracker.model.UserAccountDTO;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
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
public class UserAccountServiceJPA implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<UserAccountDTO> listUsers(String username, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<UserAccount> userPage;
        if (StringUtils.hasText(username)) {
            userPage = listUserByName(username, pageRequest);
        } else {
            userPage = userAccountRepository.findAll(pageRequest);
        }
        return userPage.map(userAccountMapper::userToUserDto);
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
        Sort sort = Sort.by(Sort.Order.asc("createdDate"));
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    public Page<UserAccount> listUserByName(String username, Pageable pageable) {
        return userAccountRepository.findAllByUsernameIsLikeIgnoreCase(username, pageable);
    }

    @Override
    public Optional<UserAccountDTO> getUserById(UUID id) {
        return Optional.ofNullable(userAccountMapper.userToUserDto(userAccountRepository.findById(id).orElse(null)));
    }

    @Override
    public UserAccountDTO saveNewUser(UserAccountDTO userAccountDTO) {
        return userAccountMapper.userToUserDto(userAccountRepository.save(userAccountMapper.userDtoToUser(userAccountDTO)));
    }

    @Override
    public Optional<UserAccountDTO> updateUserById(UUID userId, UserAccountDTO userAccountDTO) {
        AtomicReference<Optional<UserAccountDTO>> atomicReference = new AtomicReference<>();
        userAccountRepository.findById(userId).ifPresentOrElse(user -> {
            user.setEmail(userAccountDTO.getEmail());
            user.setPassword(userAccountDTO.getPassword());
            atomicReference.set(Optional.of(userAccountMapper.userToUserDto(userAccountRepository.save(user))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID userId) {
        if (userAccountRepository.existsById(userId)) {
            userAccountRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UserAccountDTO> patchUserById(UUID userId, UserAccountDTO userAccountDTO) {
        AtomicReference<Optional<UserAccountDTO>> atomicReference = new AtomicReference<>();
        userAccountRepository.findById(userId).ifPresentOrElse(foundUser -> {
            UserAccountDTO existingUserAccountDTO = userAccountMapper.userToUserDto(foundUser);

            if (StringUtils.hasText(userAccountDTO.getEmail())) {
                existingUserAccountDTO.setEmail(userAccountDTO.getEmail());
            }
            if (StringUtils.hasText(userAccountDTO.getPassword())) {
                existingUserAccountDTO.setPassword(userAccountDTO.getPassword());
            }
            atomicReference.set(Optional.of(userAccountMapper.userToUserDto(userAccountRepository.save(foundUser))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }
}
