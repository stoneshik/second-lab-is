package lab.is.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RetryInsertException extends RuntimeException {
    private final transient long recordCount;
    public RetryInsertException(String message, long recordCount) {
        super(message);
        this.recordCount = recordCount;
    }
}
