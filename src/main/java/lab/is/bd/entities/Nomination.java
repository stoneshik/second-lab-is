package lab.is.bd.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "nominations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Nomination {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nominations_seq")
    @SequenceGenerator(name = "nominations_seq", sequenceName = "nominations_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Valid
    @NotNull(message = "{not-null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "music_band_id",
        nullable = false,
        foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MusicBand musicBand;

    @ToString.Include
    @NotNull(message = "{not-null}")
    @Enumerated(EnumType.STRING)
    @Column(name = "music_genre", nullable = false)
    private MusicGenre musicGenre;

    @ToString.Include
    @NotNull(message = "{not-null}")
    @Column(name = "nominated_at", nullable = false, updatable = false)
    private LocalDateTime nominatedAt;

    @PrePersist
    protected void onCreate() {
        this.nominatedAt = LocalDateTime.now();
    }
}
