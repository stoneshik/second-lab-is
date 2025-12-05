package lab.is.services.insertion.bloomfilter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BloomFilterManager {
    private final BloomFilterTxManager bloomFilterTxManager;
    private final AtomicReference<BloomFilter<String>> bloomFilterRef;

    public BloomFilterManager(BloomFilterTxManager bloomFilterTxManager) {
        this.bloomFilterTxManager = bloomFilterTxManager;
        this.bloomFilterRef = new AtomicReference<>(createNewFilter());
    }

    @PostConstruct
    public void init() {
        rebuild();
    }

    private BloomFilter<String> createNewFilter() {
        return BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                5_000_000,
                0.000001
        );
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
        BloomFilter<String> newFilter = bloomFilterTxManager.rebuild(createNewFilter());
        bloomFilterRef.set(newFilter);
        log.info("Bloom filter перестроен");
    }
}

