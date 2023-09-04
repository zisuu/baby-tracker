package ch.finecloud.babytracker.model;

import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.entities.UserAccount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class BabyDTO {
    private UUID id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private LocalDate birthday;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private UserAccount userAccount;
    @Builder.Default
    private Set<Event> events = new HashSet<>();
}
