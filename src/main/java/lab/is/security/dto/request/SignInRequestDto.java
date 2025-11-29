package lab.is.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {
    @NotBlank(message = "{not-blank}")
    private String login;

    @NotBlank(message = "{not-blank}")
    private String password;
}
