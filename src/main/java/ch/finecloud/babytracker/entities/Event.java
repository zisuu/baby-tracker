package ch.finecloud.babytracker.entities;

import ch.finecloud.babytracker.model.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
public class Event {
    public Event(UUID id, Integer version, EventType eventType, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String notes, Baby baby) {
        this.id = id;
        this.version = version;
        this.eventType = eventType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.notes = notes;
        this.setBaby(baby);
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    @NotNull
    private EventType eventType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
    @Size(max = 255)
    @Column(length = 255)
    private String notes;

    public void setBaby(Baby baby) {
        this.baby = baby;
        baby.getEvents().add(this);
    }

    @ManyToOne
    private Baby baby;

}
