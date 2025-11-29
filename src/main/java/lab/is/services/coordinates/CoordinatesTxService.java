package lab.is.services.coordinates;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Coordinates;
import lab.is.exceptions.NestedObjectNotFoundException;
import lab.is.repositories.CoordinatesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class CoordinatesTxService {
    private final CoordinatesRepository coordinatesRepository;

    @Transactional(readOnly = true)
    public Coordinates findByIdReturnsEntity(Long id) {
        return coordinatesRepository.findById(id)
                .orElseThrow(
                    () -> new NestedObjectNotFoundException(
                        String.format("Координаты с id: %s не найдены", id)
                    )
                );
    }
}
