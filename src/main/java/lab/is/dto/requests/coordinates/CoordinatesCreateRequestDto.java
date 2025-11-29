package lab.is.dto.requests.coordinates;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoordinatesCreateRequestDto {
    @NotNull(message = "{not-null}")
    private Float x;

    @NotNull(message = "{not-null}")
    private Integer y;
}
