package lab.is.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectDtoInRequestException extends RuntimeException {
    public IncorrectDtoInRequestException(String message) {
        super(message);
    }
}
