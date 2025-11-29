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
@Table(name = "studios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Studio {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studio_seq")
    @SequenceGenerator(name = "studio_seq", sequenceName = "studio_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ToString.Include
    @NotNull(message = "{not-null}")
    @Column(name = "name", nullable = false)
    private String name;

    @ToString.Include
    @NotNull(message = "{not-null}")
    @Column(name = "address", nullable = false)
    private String address;

    @Builder.Default
    @OneToMany(
        mappedBy = "studio",
        fetch = FetchType.LAZY,
        cascade = {},
        orphanRemoval = false
    )
    private Set<MusicBand> musicBands = new HashSet<>();

    public void addMusicBand(MusicBand band) {
        if (band == null) return;
        musicBands.add(band);
        band.setStudio(this);
    }

    public void removeMusicBand(MusicBand band) {
        if (band == null) return;
        musicBands.remove(band);
        band.setStudio(null);
    }
}
