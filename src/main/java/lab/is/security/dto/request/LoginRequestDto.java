package lab.is.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    @NotBlank(message = "{not-blank}")
    @Size(min = 4, max = 40, message = "{size.login}")
    private String login;

    @NotBlank(message = "{not-blank}")
    @Size(min = 4, max = 40, message = "{size.password}")
    private String password;
}
