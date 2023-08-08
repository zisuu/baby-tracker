package ch.finecloud.babytracker.model;

import ch.finecloud.babytracker.entities.Baby;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class UserAccountDTO {
    private UUID id;
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String password;
    private String role;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    @Builder.Default
    private Set<Baby> babies = new HashSet<>();
}
