package lab.is.dto.requests.musicband;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lab.is.bd.entities.MusicGenre;
import lab.is.dto.requests.album.AlbumRequestCreateDto;
import lab.is.dto.requests.coordinates.CoordinatesCreateRequestDto;
import lab.is.dto.requests.studio.StudioRequestCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicBandRequestCreateDto {
    @NotBlank(message = "{not-blank}")
    private String name;

    @Valid
    private CoordinatesCreateRequestDto coordinates;

    @Positive(message = "{positive}")
    private Long coordinatesId;

    private MusicGenre genre;

    @Positive(message = "{positive}")
    private Long numberOfParticipants;

    @NotNull(message = "{not-null}")
    @Positive(message = "{positive}")
    private Long singlesCount;

    private String description;

    @Valid
    private AlbumRequestCreateDto bestAlbum;

    @Positive(message = "{positive}")
    private Long bestAlbumId;

    @NotNull(message = "{not-null}")
    @Positive(message = "{positive}")
    private Long albumsCount;

    @NotNull(message = "{not-null}")
    private LocalDate establishmentDate;

    @Valid
    private StudioRequestCreateDto studio;

    @Positive(message = "{positive}")
    private Long studioId;
}
