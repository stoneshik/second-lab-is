package lab.is.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CsvParserException extends RuntimeException {
    private final transient long insertionHistoryId;
    private final transient long recordCount;

    public CsvParserException(String message, long insertionHistoryId, long recordCount) {
        super(message);
        this.insertionHistoryId = insertionHistoryId;
        this.recordCount = recordCount;
    }
}
