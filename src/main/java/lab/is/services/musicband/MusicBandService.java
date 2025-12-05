package lab.is.services.musicband;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Coordinates;
import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.MusicGenre;
import lab.is.dto.requests.musicband.MusicBandRequestCreateDto;
import lab.is.dto.requests.musicband.MusicBandRequestUpdateDto;
import lab.is.dto.responses.musicband.MusicBandResponseDto;
import lab.is.dto.responses.musicband.WrapperListMusicBandResponseDto;
import lab.is.repositories.MusicBandRepository;
import lab.is.repositories.specifications.musicband.MusicBandSpecifications;
import lab.is.util.musicband.MusicBandToDtoFromEntityMapper;
import lab.is.util.musicband.MusicBandToEntityFromDtoCreateRequest;
import lab.is.util.musicband.MusicBandToEntityFromDtoUpdateRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicBandService {
    private final MusicBandRepository musicBandRepository;
    private final MusicBandTxService musicBandTxService;
    private final MusicBandNameUniquenessValidator musicBandNameUniquenessValidator;
    private final MusicBandSpecifications musicBandSpecifications;
    private final MusicBandToEntityFromDtoCreateRequest musicBandToEntityFromDtoCreateRequest;
    private final MusicBandToEntityFromDtoUpdateRequest musicBandToEntityFromDtoUpdateRequest;

    @Transactional(readOnly = true)
    public WrapperListMusicBandResponseDto findAll(
        String name,
        MusicGenre genre,
        String description,
        String bestAlbumName,
        String studioName,
        String studioAddress,
        Pageable pageable
    ) {
        Specification<MusicBand> specification = Specification.unrestricted();

        specification = specification.and(musicBandSpecifications.nameLike(name));
        specification = specification.and(musicBandSpecifications.genreEquals(genre));
        specification = specification.and(musicBandSpecifications.descriptionLike(description));
        specification = specification.and(musicBandSpecifications.bestAlbumNameLike(bestAlbumName));
        specification = specification.and(musicBandSpecifications.studioNameLike(studioName));
        specification = specification.and(musicBandSpecifications.studioAddressLike(studioAddress));

        Page<MusicBand> page = musicBandRepository.findAll(specification, pageable);
        List<MusicBandResponseDto> musicBandResponseDtos = new ArrayList<>();

        page.forEach(musicBand ->
            musicBandResponseDtos.add(
                MusicBandToDtoFromEntityMapper.toDtoFromEntity(musicBand)
            )
        );

        return WrapperListMusicBandResponseDto.builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getNumberOfElements())
            .musicBands(musicBandResponseDtos)
            .build();
    }

    @Transactional(readOnly = true)
    public MusicBandResponseDto findById(Long id) {
        MusicBand musicBand = musicBandTxService.findByIdReturnsEntity(id);
        return MusicBandToDtoFromEntityMapper.toDtoFromEntity(musicBand);
    }

    @Transactional
    public MusicBand create(MusicBandRequestCreateDto dto) {
        musicBandNameUniquenessValidator.validate(dto.getName());
        MusicBand musicBand = musicBandToEntityFromDtoCreateRequest.toEntityFromDto(dto);
        MusicBand savedMusicBand = musicBandRepository.save(musicBand);
        musicBandRepository.flush();
        return savedMusicBand;
    }

    @Transactional
    public MusicBand update(long id, MusicBandRequestUpdateDto dto) {
        MusicBand musicBand = musicBandTxService.findByIdReturnsEntity(id);
        if (!musicBand.getName().equals(dto.getName())) {
            musicBandNameUniquenessValidator.validate(dto.getName());
        }
        musicBand = musicBandToEntityFromDtoUpdateRequest.toEntityFromDto(
            dto,
            musicBand
        );
        MusicBand savedMusicBand = musicBandRepository.save(musicBand);
        musicBandRepository.flush();
        return savedMusicBand;
    }

    @Transactional
    public void delete(Long id) {
        MusicBand musicBand = musicBandTxService.findByIdReturnsEntity(id);
        Coordinates coordinates = musicBand.getCoordinates();
        coordinates.removeMusicBand(musicBand);
        musicBandRepository.delete(musicBand);
        musicBandRepository.flush();
    }

    @Transactional(readOnly = true)
    public MusicBand findByIdReturnsEntity(Long id) {
        return musicBandTxService.findByIdReturnsEntity(id);
    }
}
