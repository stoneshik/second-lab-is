package lab.is.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NestedObjectIsUsedException extends RuntimeException {
    public NestedObjectIsUsedException(String message) {
        super(message);
    }
}
