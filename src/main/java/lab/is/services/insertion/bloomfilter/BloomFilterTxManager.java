package lab.is.services.insertion.bloomfilter;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.hash.BloomFilter;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BloomFilterTxManager {
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public synchronized BloomFilter<String> rebuild(BloomFilter<String> newFilter) {
        int pageSize = 50000;
        int page = 0;
        while (true) {
            List<String> batch = entityManager.createQuery(
                "SELECT m.name FROM MusicBand m ORDER BY m.id",
                String.class
            ).setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
            if (batch.isEmpty()) {
                break;
            }
            batch.forEach(newFilter::put);
            page++;
            if (page % 5 == 0) {
                entityManager.clear();
            }
        }
        return newFilter;
    }
}
