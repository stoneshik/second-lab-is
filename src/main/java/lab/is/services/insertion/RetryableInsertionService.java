package lab.is.services.insertion;

import java.io.InputStream;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import jakarta.persistence.PersistenceException;
import lab.is.exceptions.CsvParserException;
import lab.is.exceptions.DuplicateNameException;
import lab.is.exceptions.RetryInsertException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetryableInsertionService {
    private final CsvInsertionService insertionService;
    private static final int MAX_RETRIES = 2;

    @Retryable(
        retryFor = {
            RetryInsertException.class,
            PersistenceException.class,
            DataAccessException.class,
            TransactionException.class,
            SQLException.class,
        },
        noRetryFor = {
            CsvParserException.class,
            DuplicateNameException.class
        },
        maxAttempts = MAX_RETRIES,
        backoff = @Backoff(delay = 100, multiplier = 2)
    )
    public Long insertWithRetry(InputStream csvStream, long insertionHistoryId) {
        return insertionService.insertCsv(csvStream, insertionHistoryId);
    }
}
