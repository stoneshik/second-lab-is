package lab.is.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lab.is.bd.entities.InsertionHistory;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CsvParserException extends RuntimeException {
    private final transient InsertionHistory insertionHistory;

    public CsvParserException(String message, InsertionHistory insertionHistory) {
        super(message);
        this.insertionHistory = insertionHistory;
    }

    public InsertionHistory getInsertionHistory() {
        return insertionHistory;
    }
}
