package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.UserAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountService {
    Page<UserAccountDTO> listUsers(String username, Integer pageNumber, Integer pageSize);

    Optional<UserAccountDTO> getUserById(UUID id);
    Optional<UserAccountDTO> getUserByUsername(String username);
    UserAccountDTO saveNewUser(UserAccountDTO userAccountDTO);

    Optional<UserAccountDTO> updateUserById(UUID userId, UserAccountDTO userAccountDTO);

    Boolean deleteById(UUID userId);

    Optional<UserAccountDTO> patchUserById(UUID userId, UserAccountDTO userAccountDTO);
}
