package lab.is.services.insertion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lab.is.bd.entities.MusicBand;

@Service
public class CsvInsertionParserTxService {
    private static final int BATCH_SIZE = 100;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createMusicBand(EntityManager entityManager, MusicBand musicBand, long recordCount) {
        entityManager.persist(musicBand);
        if (recordCount % BATCH_SIZE == 0) {
            entityManager.flush();
            entityManager.clear();
        }
    }
}
