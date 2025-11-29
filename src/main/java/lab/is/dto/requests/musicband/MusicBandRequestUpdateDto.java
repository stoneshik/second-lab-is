package lab.is.dto.requests.musicband;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lab.is.bd.entities.MusicGenre;
import lab.is.dto.requests.album.AlbumRequestUpdateDto;
import lab.is.dto.requests.coordinates.CoordinatesUpdateRequestDto;
import lab.is.dto.requests.studio.StudioRequestUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicBandRequestUpdateDto {
    @NotBlank(message = "{not-blank}")
    private String name;

    @Valid
    private CoordinatesUpdateRequestDto coordinates;

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
    private AlbumRequestUpdateDto bestAlbum;

    @Positive(message = "{positive}")
    private Long bestAlbumId;

    @NotNull(message = "{not-null}")
    @Positive(message = "{positive}")
    private Long albumsCount;

    @NotNull(message = "{not-null}")
    private LocalDate establishmentDate;

    @Valid
    private StudioRequestUpdateDto studio;

    @Positive(message = "{positive}")
    private Long studioId;
}
