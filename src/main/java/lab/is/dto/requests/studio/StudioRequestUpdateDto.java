package lab.is.dto.requests.studio;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudioRequestUpdateDto {
    @NotNull(message = "{not-null}")
    private String name;

    @NotNull(message = "{not-null}")
    private String address;
}
