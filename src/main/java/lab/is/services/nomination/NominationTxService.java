package lab.is.services.nomination;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Nomination;
import lab.is.exceptions.ObjectNotFoundException;
import lab.is.repositories.NominationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NominationTxService {
    private final NominationRepository nominationRepository;

    @Transactional(readOnly = true)
    public Nomination findByIdReturnsEntity(Long id) {
        return nominationRepository.findById(id)
                .orElseThrow(
                    () -> new ObjectNotFoundException(
                        String.format("Номинация с id: %s не найдена", id)
                    )
                );
    }
}
