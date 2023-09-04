package ch.finecloud.babytracker.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Baby {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;
    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(length = 50)
    private String name;
    @NotNull
    private LocalDate birthday;
    @Version
    private Integer version;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JsonBackReference
    private UserAccount userAccount;

    @Builder.Default
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "baby", cascade = CascadeType.PERSIST)
    private Set<Event> events = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Baby baby)) return false;

        if (getId() != null ? !getId().equals(baby.getId()) : baby.getId() != null) return false;
        if (getName() != null ? !getName().equals(baby.getName()) : baby.getName() != null) return false;
        if (getVersion() != null ? !getVersion().equals(baby.getVersion()) : baby.getVersion() != null) return false;
        if (getCreatedDate() != null ? !getCreatedDate().equals(baby.getCreatedDate()) : baby.getCreatedDate() != null)
            return false;
        if (getLastModifiedDate() != null ? !getLastModifiedDate().equals(baby.getLastModifiedDate()) : baby.getLastModifiedDate() != null)
            return false;
        if (getUserAccount() != null ? !getUserAccount().equals(baby.getUserAccount()) : baby.getUserAccount() != null)
            return false;
        return getEvents() != null ? getEvents().equals(baby.getEvents()) : baby.getEvents() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
        result = 31 * result + (getLastModifiedDate() != null ? getLastModifiedDate().hashCode() : 0);
        return result;
    }
}
