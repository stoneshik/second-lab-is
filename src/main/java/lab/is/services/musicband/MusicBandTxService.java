package lab.is.services.musicband;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.MusicBand;
import lab.is.exceptions.NestedObjectNotFoundException;
import lab.is.repositories.MusicBandRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicBandTxService {
    private final MusicBandRepository musicBandRepository;

    @Transactional(readOnly = true)
    public MusicBand findByIdReturnsEntity(Long id) {
        return musicBandRepository.findById(id)
                .orElseThrow(
                    () -> new NestedObjectNotFoundException(
                        String.format("Музыкальная группа с id: %s не найдена", id)
                    )
                );
    }
}
