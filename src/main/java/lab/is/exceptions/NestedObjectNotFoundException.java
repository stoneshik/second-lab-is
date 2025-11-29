package lab.is.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NestedObjectNotFoundException extends ObjectNotFoundException {
    public NestedObjectNotFoundException(String message) {
        super(message);
    }
}
