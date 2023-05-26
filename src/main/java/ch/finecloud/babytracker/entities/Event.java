package ch.finecloud.babytracker.entities;

import ch.finecloud.babytracker.model.EventType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    @NotNull
    @Enumerated(EnumType.STRING)
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

    @ManyToOne
    @JsonBackReference
    private Baby baby;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;

        if (getId() != null ? !getId().equals(event.getId()) : event.getId() != null) return false;
        if (getVersion() != null ? !getVersion().equals(event.getVersion()) : event.getVersion() != null) return false;
        if (getEventType() != event.getEventType()) return false;
        if (getStartDate() != null ? !getStartDate().equals(event.getStartDate()) : event.getStartDate() != null)
            return false;
        if (getEndDate() != null ? !getEndDate().equals(event.getEndDate()) : event.getEndDate() != null) return false;
        if (getCreatedDate() != null ? !getCreatedDate().equals(event.getCreatedDate()) : event.getCreatedDate() != null)
            return false;
        if (getLastModifiedDate() != null ? !getLastModifiedDate().equals(event.getLastModifiedDate()) : event.getLastModifiedDate() != null)
            return false;
        if (getNotes() != null ? !getNotes().equals(event.getNotes()) : event.getNotes() != null) return false;
        return getBaby() != null ? getBaby().equals(event.getBaby()) : event.getBaby() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + (getEventType() != null ? getEventType().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
        result = 31 * result + (getLastModifiedDate() != null ? getLastModifiedDate().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        return result;
    }
}
