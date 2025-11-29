package lab.is.services.coordinates;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Coordinates;
import lab.is.dto.requests.coordinates.CoordinatesUpdateRequestDto;
import lab.is.dto.responses.coordinates.CoordinatesResponseDto;
import lab.is.dto.responses.coordinates.WrapperListCoordinatesResponseDto;
import lab.is.exceptions.NestedObjectIsUsedException;
import lab.is.repositories.CoordinatesRepository;
import lab.is.util.CoordinatesMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;
    private final CoordinatesTxService coordinatesTxService;

    @Transactional(readOnly = true)
    public WrapperListCoordinatesResponseDto findAll(Pageable pageable) {
        Page<Coordinates> page = coordinatesRepository.findAll(pageable);
        List<CoordinatesResponseDto> coordinatesResponseDtos = new ArrayList<>();

        page.forEach(coordinates ->
            coordinatesResponseDtos.add(
                CoordinatesMapper.toDtoFromEntity(coordinates)
            )
        );

        return WrapperListCoordinatesResponseDto.builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getNumberOfElements())
            .coordinates(coordinatesResponseDtos)
            .build();
    }

    @Transactional(readOnly = true)
    public CoordinatesResponseDto findById(Long id) {
        Coordinates coordinates = coordinatesTxService.findByIdReturnsEntity(id);
        return CoordinatesMapper.toDtoFromEntity(coordinates);
    }

    @Transactional
    public Coordinates create(Float x, int y) {
        Coordinates coordinates = Coordinates.builder()
            .x(x)
            .y(y)
            .build();
        Coordinates savedCoordinates = coordinatesRepository.save(coordinates);
        coordinatesRepository.flush();
        return savedCoordinates;
    }

    @Transactional
    public Coordinates update(long id, CoordinatesUpdateRequestDto dto) {
        Coordinates coordinates = coordinatesTxService.findByIdReturnsEntity(id);
        Coordinates updatedCoordinates = coordinates.toBuilder()
            .x(dto.getX())
            .y(dto.getY())
            .build();
        Coordinates savedCoordinates = coordinatesRepository.save(updatedCoordinates);
        coordinatesRepository.flush();
        return savedCoordinates;
    }

    @Transactional
    public void delete(Long id) {
        Coordinates coordinates = coordinatesTxService.findByIdReturnsEntity(id);
        if (isUsedNestedObject(coordinates)) {
            throw new NestedObjectIsUsedException(
                String.format(
                    "Координаты с id: %s не могут быть удалены, так как связаны с другими объектами",
                    id
                )
            );
        }
        coordinatesRepository.delete(coordinates);
        coordinatesRepository.flush();
    }

    @Transactional(readOnly = true)
    public Coordinates findByIdReturnsEntity(Long id) {
        return coordinatesTxService.findByIdReturnsEntity(id);
    }

    private boolean isUsedNestedObject(Coordinates coordinates) {
        return !coordinates.getMusicBands().isEmpty();
    }
}
