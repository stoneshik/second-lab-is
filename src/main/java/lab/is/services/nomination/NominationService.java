package lab.is.services.nomination;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.MusicGenre;
import lab.is.bd.entities.Nomination;
import lab.is.dto.responses.nomination.NominationResponseDto;
import lab.is.dto.responses.nomination.WrapperListNominationResponseDto;
import lab.is.exceptions.ObjectNotFoundException;
import lab.is.repositories.MusicBandRepository;
import lab.is.repositories.NominationRepository;
import lab.is.repositories.specifications.nomination.NominationSpecifications;
import lab.is.util.NominationMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NominationService {
    private final NominationRepository nominationRepository;
    private final MusicBandRepository musicBandRepository;
    private final NominationTxService nominationTxService;

    private final NominationSpecifications nominationSpecifications;

    @Transactional(readOnly = true)
    public WrapperListNominationResponseDto findAll(
            Long musicBandId,
            MusicGenre genre,
            LocalDateTime nominatedFrom,
            LocalDateTime nominatedTo,
            Pageable pageable
    ) {
        Specification<Nomination> spec = Specification.unrestricted();

        spec = spec.and(nominationSpecifications.musicBandIdEquals(musicBandId));
        spec = spec.and(nominationSpecifications.genreEquals(genre));
        spec = spec.and(nominationSpecifications.nominatedAfterOrEq(nominatedFrom));
        spec = spec.and(nominationSpecifications.nominatedBeforeOrEq(nominatedTo));

        Page<Nomination> page = nominationRepository.findAll(spec, pageable);
        List<NominationResponseDto> nominationResponseDtos = new ArrayList<>();

        page.forEach(nomination ->
            nominationResponseDtos.add(
                NominationMapper.toDtoFromEntity(nomination)
            )
        );

        return WrapperListNominationResponseDto.builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getNumberOfElements())
            .nominations(nominationResponseDtos)
            .build();
    }

    @Transactional(readOnly = true)
    public NominationResponseDto findById(Long id) {
        Nomination nomination = nominationTxService.findByIdReturnsEntity(id);
        return NominationMapper.toDtoFromEntity(nomination);
    }

    @Transactional
    public Nomination create(Long musicBandId, MusicGenre musicGenre) {
        MusicBand foundMusicBand = musicBandRepository.findById(musicBandId)
            .orElseThrow(
                () -> new ObjectNotFoundException(
                    String.format("Музыкальная группа с id: %s не найдена", musicBandId)
                )
            );

        Nomination nomination = Nomination.builder()
            .musicBand(foundMusicBand)
            .musicGenre(musicGenre)
            .build();

        Nomination savedNomination = nominationRepository.save(nomination);
        nominationRepository.flush();
        return savedNomination;
    }

    @Transactional
    public void delete(Long id) {
        Nomination nomination = nominationTxService.findByIdReturnsEntity(id);
        nominationRepository.delete(nomination);
        nominationRepository.flush();
    }

    @Transactional(readOnly = true)
    public Nomination findByIdReturnsEntity(Long id) {
        return nominationTxService.findByIdReturnsEntity(id);
    }
}
