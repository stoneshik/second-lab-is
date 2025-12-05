package lab.is.services.insertion;

import java.io.InputStream;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import jakarta.persistence.OptimisticLockException;
import lab.is.bd.entities.InsertionHistory;
import lab.is.exceptions.RetryInsertException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetryableInsertionService {
    private final CsvInsertionService insertionService;
    private static final int MAX_RETRIES = 1;

    @Retryable(
        retryFor = {OptimisticLockException.class},
        maxAttempts = MAX_RETRIES,
        backoff = @Backoff(delay = 100, multiplier = 2)
    )
    public Long insertWithRetry(InputStream csvStream, InsertionHistory history) {
        return insertionService.insertCsv(csvStream, history);
    }

    @Recover
    public Long recoverInsertion(InputStream csvStream, InsertionHistory history) {
        throw new RetryInsertException(
            String.format("объект был изменен другим пользователем, вставка не удалась после %s попыток", MAX_RETRIES)
        );
    }
}
