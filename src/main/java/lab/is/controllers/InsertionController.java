package lab.is.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lab.is.exceptions.CsvParserException;
import lab.is.services.insertion.CsvInsertionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/insertion")
@RequiredArgsConstructor
public class InsertionController {
    private final CsvInsertionService insertionService;

    @PostMapping("/csv")
    public ResponseEntity<Void> importCsv(@RequestParam MultipartFile file) {
        try {
            insertionService.insertCsv(file.getInputStream());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new CsvParserException("Ошибка при импорте данных");
        }
    }
}
