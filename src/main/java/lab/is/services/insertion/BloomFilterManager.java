package lab.is.services.insertion;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BloomFilterManager {
    @PersistenceContext
    private final EntityManager entityManager;
    private final AtomicReference<BloomFilter<String>> bloomFilterRef;

    @PostConstruct
    public void init() {
        rebuild();
    }

    public boolean mightContain(String name) {
        if (name == null || name.isBlank()) return false;
        return bloomFilterRef.get().mightContain(name);
    }

    public void put(String name) {
        if (name == null || name.isBlank()) return;
        bloomFilterRef.get().put(name);
    }

    public void putAll(Iterable<String> names) {
        BloomFilter<String> filter = bloomFilterRef.get();
        for (String name : names) {
            if (name != null && !name.isBlank()) {
                filter.put(name);
            }
        }
    }

    public synchronized void rebuild() {
        log.info("Пересборка Bloom filter...");
        BloomFilter<String> newFilter = createNewFilter();
        entityManager.createQuery(
                "SELECT m.name FROM MusicBand m WHERE m.name",
                String.class
            )
            .getResultStream()
            .forEach(newFilter::put);
        bloomFilterRef.set(newFilter);
        log.info("Bloom filter перестроен");
    }

    private BloomFilter<String> createNewFilter() {
        return BloomFilter.create(
            Funnels.stringFunnel(StandardCharsets.UTF_8),
            1_500_000,
            0.0000001
        );
    }
}
