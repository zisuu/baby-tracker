package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.UserAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserAccountServiceImpl implements UserAccountService {


    private final Map<UUID, UserAccountDTO> userMap;

    public UserAccountServiceImpl() {
        this.userMap = new HashMap<>();

        UserAccountDTO user1 = UserAccountDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .email("user1@example.com")
                .password("password1")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        UserAccountDTO user2 = UserAccountDTO.builder()
                .id(UUID.randomUUID())
                .version(2)
                .email("user2@example.com")
                .password("password2")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        UserAccountDTO user3 = UserAccountDTO.builder()
                .id(UUID.randomUUID())
                .version(3)
                .email("user3@example.com")
                .password("password3")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        userMap.put(user1.getId(), user1);
        userMap.put(user2.getId(), user2);
        userMap.put(user3.getId(), user3);
    }
    @Override
    public Page<UserAccountDTO> listUsers(String email, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(userMap.values()));
    }

    @Override
    public Optional<UserAccountDTO> getUserById(UUID id) {
        log.debug("getUserById was called with id: " + String.valueOf(id) + ", in Service");
        return Optional.of(userMap.get(id));
    }

    @Override
    public Optional<UserAccountDTO> getUserByUsername(String username) {
        log.debug("getUserByUsername was called with username: " + username + ", in Service");
        return userMap.values().stream()
                .filter(userAccountDTO -> userAccountDTO.getEmail().equals(username))
                .findFirst();
    }

    @Override
    public UserAccountDTO saveNewUser(UserAccountDTO userAccountDTO) {
        UserAccountDTO savedUserAccountDTO = UserAccountDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .email(userAccountDTO.getEmail())
                .password(userAccountDTO.getPassword())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        userMap.put(savedUserAccountDTO.getId(), savedUserAccountDTO);
        return savedUserAccountDTO;
    }

    @Override
    public Optional<UserAccountDTO> updateUserById(UUID userId, UserAccountDTO userAccountDTO) {
        UserAccountDTO existingUserAccountDTO = userMap.get(userId);
        existingUserAccountDTO.setPassword(userAccountDTO.getPassword());
        existingUserAccountDTO.setEmail(userAccountDTO.getPassword());
        userMap.put(existingUserAccountDTO.getId(), existingUserAccountDTO);
        return Optional.of(existingUserAccountDTO);
    }

    @Override
    public Boolean deleteById(UUID userId) {
        userMap.remove(userId);
        return true;
    }

    @Override
    public Optional<UserAccountDTO> patchUserById(UUID userId, UserAccountDTO userAccountDTO) {
        UserAccountDTO existingUser = userMap.get(userId);

        if(StringUtils.hasText(userAccountDTO.getPassword())) {
            existingUser.setPassword(userAccountDTO.getPassword());
        }

        if(StringUtils.hasText(userAccountDTO.getEmail())) {
            existingUser.setEmail(userAccountDTO.getEmail());
        }

        return Optional.of(existingUser);
    }

}
