package ch.finecloud.babytracker.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class BabyDTO {
    private UUID id;
    private String name;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
