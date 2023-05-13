package ch.finecloud.babytracker.entities;

import ch.finecloud.babytracker.model.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    @NotNull
    private EventType eventType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lastModifiedDate;
    @Size(max = 255)
    @Column(length = 255)
    private String notes;
}
