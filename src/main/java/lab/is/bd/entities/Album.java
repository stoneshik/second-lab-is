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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "albums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Album {
    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "album_seq")
    @SequenceGenerator(name = "album_seq", sequenceName = "album_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ToString.Include
    @NotBlank(message = "{not-blank}")
    @Column(name = "name", nullable = false)
    private String name;

    @ToString.Include
    @Positive(message = "{positive}")
    @Column(name = "length", nullable = false)
    private int length;

    @Builder.Default
    @OneToMany(
        mappedBy = "bestAlbum",
        fetch = FetchType.LAZY,
        cascade = {},
        orphanRemoval = false
    )
    private Set<MusicBand> musicBands = new HashSet<>();

    public void addMusicBand(MusicBand band) {
        if (band == null) return;
        musicBands.add(band);
        band.setBestAlbum(this);
    }

    public void removeMusicBand(MusicBand band) {
        if (band == null) return;
        musicBands.remove(band);
        band.setBestAlbum(null);
    }
}
