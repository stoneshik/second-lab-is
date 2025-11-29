package lab.is.services.musicband;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.MusicBand;
import lab.is.dto.responses.musicband.MusicBandResponseDto;
import lab.is.exceptions.ObjectNotFoundException;
import lab.is.exceptions.ValueOverflowException;
import lab.is.repositories.MusicBandRepository;
import lab.is.util.musicband.MusicBandToDtoFromEntityMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicBandAdditionalFunctionalityService {
    private final MusicBandRepository musicBandRepository;

    @Transactional
    public boolean deleteOneByEstablishmentDate(LocalDate date) {
        return musicBandRepository.findFirstByEstablishmentDate(date)
            .map(entity -> {
                musicBandRepository.delete(entity);
                return true;
            })
            .orElse(false);
    }

    @Transactional(readOnly = true)
    public MusicBandResponseDto findOneWithMinId() {
        MusicBand band = musicBandRepository.findFirstByOrderByIdAsc()
            .orElseThrow(() ->
                new ObjectNotFoundException("Музыкальная группа не найдена")
            );
        return MusicBandToDtoFromEntityMapper.toDtoFromEntity(band);
    }

    @Transactional(readOnly = true)
    public List<MusicBandResponseDto> findAllWithEstablishmentDateAfter(LocalDate date) {
        List<MusicBand> foundMusicBands = musicBandRepository.findByEstablishmentDateAfter(date);
        return foundMusicBands.stream()
            .map(MusicBandToDtoFromEntityMapper::toDtoFromEntity)
            .toList();
    }


    @Transactional
    public MusicBandResponseDto addSingleToBand(Long musicBandId) {
        MusicBand band = musicBandRepository.findById(musicBandId)
            .orElseThrow(() ->
                new ObjectNotFoundException(
                    String.format("Музыкальная группа c id %s не найдена", musicBandId)
                )
            );
        Long currentSinglesCountValue = band.getSinglesCount() == null ? 0L : band.getSinglesCount();
        if (currentSinglesCountValue >= Long.MAX_VALUE) {
            throw new ValueOverflowException(
                String.format(
                    "Невозможно добавить сингл: группа %s уже достигла максимального количества синглов",
                    band.getName()
                )
            );
        }
        band = band.toBuilder()
            .singlesCount(currentSinglesCountValue + 1)
            .build();

        MusicBand musicBandSaved = musicBandRepository.save(band);
        musicBandRepository.flush();
        return MusicBandToDtoFromEntityMapper.toDtoFromEntity(musicBandSaved);
    }
}
