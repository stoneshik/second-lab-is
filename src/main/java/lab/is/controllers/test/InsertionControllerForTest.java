package lab.is.controllers.test;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lab.is.exceptions.CsvParserException;
import lab.is.exceptions.RetryInsertException;
import lab.is.services.insertion.RetryableInsertionService;
import lab.is.services.insertion.history.InsertionHistoryService;
import lombok.RequiredArgsConstructor;

@Profile({"test", "jmeter"})
@RestController
@RequestMapping("/api/v1/insertion")
@RequiredArgsConstructor
public class InsertionControllerForTest {
    private final RetryableInsertionService insertionService;
    private final InsertionHistoryService insertionHistoryService;

    @PostMapping("/csv")
    public ResponseEntity<Void> importCsv(
        @RequestParam Long userId,
        @RequestParam MultipartFile file
    ) {
        long insertionHistoryId = insertionHistoryService.create(userId);
        try {
            Long numberObjects = insertionService.insertWithRetry(file.getInputStream(), insertionHistoryId);
            insertionHistoryService.updateStatusToSuccess(insertionHistoryId, numberObjects);
            return ResponseEntity.ok().build();
        } catch (RetryInsertException e) {
            throw new CsvParserException(
                e.getMessage(),
                insertionHistoryId,
                e.getRecordCount()
            );
        } catch (CsvParserException e) {
            throw e;
        } catch (Exception e) {
            throw new CsvParserException(
                "Ошибка при импорте данных",
                insertionHistoryId,
                0L
            );
        }
    }
}
