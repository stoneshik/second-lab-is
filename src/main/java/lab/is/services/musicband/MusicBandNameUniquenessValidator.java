package lab.is.services.musicband;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lab.is.exceptions.DuplicateNameException;
import lab.is.services.insertion.bloomfilter.BloomFilterManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicBandNameUniquenessValidator {
    @PersistenceContext
    private final EntityManager entityManager;
    private final BloomFilterManager bloomFilter;

    public void validate(String name) {
        if (name == null || name.isBlank()) {
            return;
        }
        boolean exists = false;
        if (bloomFilter.mightContain(name)) {
            exists = checkInDatabaseWithLock(name);
        }
        if (exists) {
            throw new DuplicateNameException(
                "музыкальная группа с именем '" + name + "' уже существует"
            );
        }
    }

    public void validateWithoutBloomFilter(String name) {
        if (name == null || name.isBlank()) {
            return;
        }
        boolean exists = checkInDatabaseWithLock(name);
        if (exists) {
            throw new DuplicateNameException(
                "музыкальная группа с именем '" + name + "' уже существует"
            );
        }
    }

    public boolean checkInDatabaseWithLock(String name) {
        return !entityManager.createQuery(
            "SELECT 1 FROM MusicBand m WHERE m.name = :name",
            Integer.class
        )
            .setParameter("name", name)
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .setMaxResults(1)
            .getResultList()
            .isEmpty();
    }
}
