package lab.is.dto.responses.albums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponseDto {
    private Long id;
    private String name;
    private int length;
}
