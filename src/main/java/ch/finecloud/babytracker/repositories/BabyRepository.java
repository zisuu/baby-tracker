package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BabyRepository extends JpaRepository<Baby, UUID> {
    Page<Baby> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);
    Page<Baby> findAllByUserAccount_EmailAndNameIsLikeIgnoreCase(String userAccountEmail, String name, Pageable pageable);
    Baby findBabyByName(String name);
    Page<Baby> findAllByUserAccount_Email(String userAccountEmail, PageRequest pageRequest);
}
