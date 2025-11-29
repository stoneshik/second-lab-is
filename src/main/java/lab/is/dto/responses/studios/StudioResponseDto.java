package lab.is.dto.responses.studios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudioResponseDto {
    private Long id;
    private String name;
    private String address;
}
