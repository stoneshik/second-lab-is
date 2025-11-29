package lab.is.services.studio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Studio;
import lab.is.exceptions.NestedObjectNotFoundException;
import lab.is.repositories.StudioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class StudioTxService {
    private final StudioRepository studioRepository;

    @Transactional(readOnly = true)
    public Studio findByIdReturnsEntity(Long id) {
        return studioRepository.findById(id)
                .orElseThrow(
                    () ->
                    new NestedObjectNotFoundException(
                        String.format("Студия с id: %s не найдена", id)
                    )
                );
    }
}
