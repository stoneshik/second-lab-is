package lab.is.dto.requests.nomination;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lab.is.bd.entities.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NominationCreateRequestDto {
    @NotNull(message = "{not-null}")
    @Positive(message = "{positive}")
    private Long musicBandId;

    @NotNull(message = "{not-null}")
    private MusicGenre musicGenre;
}
