package lab.is.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ValueOverflowException extends RuntimeException {
    public ValueOverflowException(String message) {
        super(message);
    }
}
