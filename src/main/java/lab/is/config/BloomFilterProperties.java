package lab.is.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class BloomFilterProperties {
    @Value("${bloom-filter.expected-insertions:5000000}")
    private int expectedInsertions;

    @Value("${bloom-filter.false-positive-probability:0.000001}")
    private double falsePositiveProbability;
}
