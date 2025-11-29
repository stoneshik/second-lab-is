package lab.is.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceIsAlreadyExistsException extends RuntimeException {
    public ResourceIsAlreadyExistsException(String message) {
        super(message);
    }
}
