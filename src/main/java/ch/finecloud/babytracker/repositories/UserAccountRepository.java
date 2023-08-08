package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.entities.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    Page<UserAccount> findAllByUsernameIsLikeIgnoreCase(String username, Pageable pageable);

    Optional<UserAccount> findUserAccountByEmail(String email);
}
