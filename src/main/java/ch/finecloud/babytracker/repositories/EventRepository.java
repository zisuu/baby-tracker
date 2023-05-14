package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>  {
    Page<Event> findAllByEventType(EventType eventType, Pageable pageable);
}
