package lab.is.dto.responses.coordinates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoordinatesResponseDto {
    private Long id;
    private Float x;
    private int y;
}
