package lab.is.services.insertion;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import lab.is.config.BatchProperties;
import lab.is.exceptions.CsvParserException;
import lab.is.exceptions.DuplicateNameException;
import lab.is.services.insertion.bloomfilter.BloomFilterManager;
import lab.is.services.musicband.MusicBandNameUniquenessValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvInsertionService {
    private final BatchProperties properties;
    @PersistenceContext
    private EntityManager entityManager;
    private final BloomFilterManager bloomFilterManager;
    private final MusicBandNameUniquenessValidator musicBandNameUniquenessValidator;
    private final String[] headers = InsertionHeaders.getHeaders();

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long insertCsv(InputStream csvStream, InsertionHistory insertionHistory) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setDelimiter(';')
            .setHeader(headers)
            .setSkipHeaderRecord(true)
            .setTrim(true)
            .setNullString("")
            .get();
        Set<String> batchNamesCache = new HashSet<>(properties.getBatchSize() * 2);
        List<MusicBand> batch = new ArrayList<>(properties.getBatchSize());
        long recordCount = 0L;
        try (Reader reader = new InputStreamReader(csvStream, StandardCharsets.UTF_8);
            CSVParser parser = CSVParser.builder()
                .setReader(reader)
                .setFormat(format)
                .get()
            ) {
            for (CSVRecord csvRecord: parser) {
                recordCount++;
                MusicBand musicBand = CsvParser.convertRecordToEntity(
                    csvRecord,
                    csvRecord.getRecordNumber(),
                    insertionHistory
                );
                String name = csvRecord.get(InsertionHeaders.NAME.getName());
                if (batchNamesCache.contains(name)) {
                    throw new DuplicateNameException(
                        String.format("Дубликат имени %s", name)
                    );
                }
                musicBandNameUniquenessValidator.validate(name);
                batchNamesCache.add(name);
                batch.add(musicBand);
                if (batch.size() >= properties.getBatchSize()) {
                    flushBatch(batch, batchNamesCache);
                    batch.clear();
                    batchNamesCache.clear();
                }
            }
            if (!batch.isEmpty()) {
                flushBatch(batch, batchNamesCache);
            }
        } catch (DuplicateNameException e) {
            throw new CsvParserException(
                String.format("Импорт прерван на строке %s: %s", recordCount, e.getMessage()),
                insertionHistory,
                recordCount
            );
        } catch (CsvParserException e) {
            throw e;
        } catch (Exception e) {
            throw new CsvParserException("Импорт прерван на строке " + recordCount, insertionHistory, recordCount);
        }
        return recordCount;
    }

    private void flushBatch(List<MusicBand> batch, Set<String> namesInBatch) {
        for (MusicBand band : batch) {
            entityManager.persist(band);
        }
        entityManager.flush();
        entityManager.clear();
        bloomFilterManager.putAll(namesInBatch);
    }
}
