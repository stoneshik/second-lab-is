package lab.is.dto.responses.nomination;

import java.time.LocalDateTime;

import lab.is.bd.entities.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NominationResponseDto {
    private Long id;
    private Long musicBandId;
    private String musicBandName;
    private MusicGenre musicGenre;
    private LocalDateTime nominatedAt;
}
