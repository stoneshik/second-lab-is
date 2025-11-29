package lab.is.dto.responses;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessageResponseDto {
    private Date timestamp;
    private String message;
    @Builder.Default
    private Map<String, String> violations = new HashMap<>();
}
