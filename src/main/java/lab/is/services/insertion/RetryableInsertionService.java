package lab.is.services.insertion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import lab.is.exceptions.CsvParserException;
import org.springframework.stereotype.Service;

import lab.is.exceptions.RetryInsertException;
import lab.is.services.insertion.bloomfilter.BloomFilterManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryableInsertionService {
    private static final int MAX_RETRIES = 3;
    private final CsvInsertionService insertionService;
    private final BloomFilterManager bloomFilterManager;

    public Long insertWithRetry(InputStream csvStream, long insertionHistoryId) {
        byte[] csvData;
        try {
            csvData = csvStream.readAllBytes();
        } catch (IOException e) {
            throw new CsvParserException("ошибка с файлом при импорте", insertionHistoryId, 0L);
        }
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try (InputStream freshStream = new ByteArrayInputStream(csvData)) {
                return insertionService.insertCsv(freshStream, insertionHistoryId);
            } catch (RetryInsertException e) {
                attempt++;
                bloomFilterManager.rebuild();
                if (attempt == MAX_RETRIES) {
                    throw new RetryInsertException(
                        String.format("Не получилось после %s попыток: %s", MAX_RETRIES, e.getMessage()),
                        e.getRecordCount()
                    );
                }
            } catch (IOException e) {
                throw new CsvParserException("ошибка с файлом при импорте", insertionHistoryId, 0L);
            }
        }
        throw new RetryInsertException("импорт не удался", 0L);
    }
}
