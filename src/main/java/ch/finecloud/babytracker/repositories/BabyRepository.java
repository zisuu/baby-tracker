package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.entities.Baby;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BabyRepository extends JpaRepository<Baby, UUID> {
    Page<Baby> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);
}
