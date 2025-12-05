package lab.is.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lab.is.bd.entities.InsertionHistory;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CsvParserException extends RuntimeException {
    private final transient InsertionHistory insertionHistory;
    private final transient long recordCount;

    public CsvParserException(String message, InsertionHistory insertionHistory, long recordCount) {
        super(message);
        this.insertionHistory = insertionHistory;
        this.recordCount = recordCount;
    }

    public InsertionHistory getInsertionHistory() {
        return insertionHistory;
    }

    public long getRecordNumber() {
        return recordCount;
    }
}
