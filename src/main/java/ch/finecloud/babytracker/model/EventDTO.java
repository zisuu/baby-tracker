package ch.finecloud.babytracker.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class EventDTO {
    private UUID id;
    private Integer version;
    private EventType eventType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lastModifiedDate;
    private String notes;
}
