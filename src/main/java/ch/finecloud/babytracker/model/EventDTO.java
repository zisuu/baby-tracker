package ch.finecloud.babytracker.model;

import ch.finecloud.babytracker.entities.Baby;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class EventDTO {
    private UUID id;
    private Integer version;
    @NotNull
    private EventType eventType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String notes;
    private Baby baby;
}
