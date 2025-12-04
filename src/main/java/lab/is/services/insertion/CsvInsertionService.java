package lab.is.services.insertion;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lab.is.bd.entities.InsertionHistory;
import lab.is.bd.entities.MusicBand;
import lab.is.exceptions.CsvParserException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvInsertionService {
    private static final int BATCH_SIZE = 100;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Long insertCsv(InputStream csvStream, InsertionHistory insertionHistory) {
        String[] headers = {
            InsertionHeaders.NAME.getName(),
            InsertionHeaders.GENRE.getName(),
            InsertionHeaders.NUMBER_OF_PARTICIPANTS.getName(),
            InsertionHeaders.SINGLES_COUNT.getName(),
            InsertionHeaders.ALBUMS_COUNT.getName(),
            InsertionHeaders.ESTABLISHMENT_DATE.getName(),
            InsertionHeaders.DESCRIPTION.getName(),
            InsertionHeaders.COORDINATES_X.getName(),
            InsertionHeaders.COORDINATES_Y.getName(),
            InsertionHeaders.STUDIO_NAME.getName(),
            InsertionHeaders.STUDIO_ADDRESS.getName(),
            InsertionHeaders.BEST_ALBUM_NAME.getName(),
            InsertionHeaders.BEST_ALBUM_LENGTH.getName()
        };
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setDelimiter(';')
            .setHeader(headers)
            .setSkipHeaderRecord(true)
            .setTrim(true)
            .setNullString("")
            .get();
        long recordCount = 0L;
        try (Reader reader = new InputStreamReader(csvStream, StandardCharsets.UTF_8);
            CSVParser parser = CSVParser.builder()
                .setReader(reader)
                .setFormat(format)
                .get()
            ) {
            for (CSVRecord csvRecord: parser) {
                recordCount++;
                MusicBand band = CsvParser.convertRecordToEntity(
                    csvRecord,
                    csvRecord.getRecordNumber(),
                    insertionHistory
                );
                entityManager.persist(band);
                if (recordCount % BATCH_SIZE == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
        } catch (CsvParserException e) {
            throw e;
        } catch (Exception e) {
            throw new CsvParserException("Импорт прерван на строке " + recordCount, insertionHistory);
        }
        return recordCount;
    }
}
