package lab.is.services.studio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Studio;
import lab.is.dto.requests.studio.StudioRequestUpdateDto;
import lab.is.dto.responses.studios.StudioResponseDto;
import lab.is.dto.responses.studios.WrapperListStudioResponseDto;
import lab.is.exceptions.NestedObjectIsUsedException;
import lab.is.repositories.StudioRepository;
import lab.is.util.StudioMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;
    private final StudioTxService studioTxService;

    @Transactional(readOnly = true)
    public WrapperListStudioResponseDto findAll(Pageable pageable) {
        Page<Studio> page = studioRepository.findAll(pageable);
        List<StudioResponseDto> studioResponseDtos = new ArrayList<>();

        page.forEach(studio ->
            studioResponseDtos.add(
                StudioMapper.toDtoFromEntity(studio)
            )
        );

        return WrapperListStudioResponseDto.builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getNumberOfElements())
            .studios(studioResponseDtos)
            .build();
    }

    @Transactional(readOnly = true)
    public StudioResponseDto findById(Long id) {
        Studio studio = studioTxService.findByIdReturnsEntity(id);
        return StudioMapper.toDtoFromEntity(studio);
    }

    @Transactional
    public Studio create(String name, String address) {
        Studio studio = Studio.builder()
            .name(name)
            .address(address)
            .build();
        Studio savedStudio = studioRepository.save(studio);
        studioRepository.flush();
        return savedStudio;
    }

    @Transactional
    public Studio update(long id, StudioRequestUpdateDto dto) {
        Studio studio = studioTxService.findByIdReturnsEntity(id);
        Studio updatedCoordinates = studio.toBuilder()
            .name(dto.getName())
            .address(dto.getAddress())
            .build();
        Studio savedStudio = studioRepository.save(updatedCoordinates);
        studioRepository.flush();
        return savedStudio;
    }

    @Transactional
    public void delete(Long id) {
        Studio studio = studioTxService.findByIdReturnsEntity(id);
        if (isUsedNestedObject(studio)) {
            throw new NestedObjectIsUsedException(
                String.format(
                    "Студия с id: %s не может быть удалена, так как связана с другими объектами",
                    id
                )
            );
        }
        studioRepository.delete(studio);
        studioRepository.flush();
    }

    @Transactional(readOnly = true)
    public Studio findByIdReturnsEntity(Long id) {
        return studioTxService.findByIdReturnsEntity(id);
    }

    private boolean isUsedNestedObject(Studio studio) {
        return !studio.getMusicBands().isEmpty();
    }
}
