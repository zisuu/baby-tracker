package ch.finecloud.babytracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class BabyDTO {
    private UUID id;
    @NotNull
    @NotBlank
    private String name;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
