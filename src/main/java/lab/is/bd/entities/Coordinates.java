package lab.is.bd.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "coordinates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Coordinates {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coordinates_seq")
    @SequenceGenerator(name = "coordinates_seq", sequenceName = "coordinates_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ToString.Include
    @NotNull(message = "{not-null}")
    @Column(name = "x", nullable = false)
    private Float x;

    @ToString.Include
    @Column(name = "y", nullable = false)
    private int y;

    @Builder.Default
    @OneToMany(
        mappedBy = "coordinates",
        fetch = FetchType.LAZY,
        cascade = {},
        orphanRemoval = false
    )
    private Set<MusicBand> musicBands = new HashSet<>();

    public void addMusicBand(MusicBand band) {
        if (band == null) return;
        musicBands.add(band);
        band.setCoordinates(this);
    }

    public void removeMusicBand(MusicBand band) {
        if (band == null) return;
        musicBands.remove(band);
        band.setCoordinates(null);
    }
}
