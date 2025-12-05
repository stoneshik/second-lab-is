package lab.is.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class BatchProperties {
    @Value("${batch-properties.batch-size:10000}")
    private int batchSize;

    @Value("${batch-properties.max-record-number_for_rebuild_bloom_filter:1000}")
    private int maxRecordNumberForRebuildBloomFilter;
}
