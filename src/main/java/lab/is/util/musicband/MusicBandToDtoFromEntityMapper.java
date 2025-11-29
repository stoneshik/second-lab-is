package lab.is.util.musicband;

import org.springframework.stereotype.Service;

import lab.is.bd.entities.MusicBand;
import lab.is.dto.responses.musicband.MusicBandResponseDto;
import lab.is.util.AlbumMapper;
import lab.is.util.CoordinatesMapper;
import lab.is.util.StudioMapper;

@Service
public class MusicBandToDtoFromEntityMapper {
    private MusicBandToDtoFromEntityMapper() {}

    public static MusicBandResponseDto toDtoFromEntity(MusicBand entity) {
        return MusicBandResponseDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .coordinates(
                CoordinatesMapper.toDtoFromEntity(
                    entity.getCoordinates()
                )
            )
            .creationDate(entity.getCreationDate())
            .genre(entity.getGenre())
            .numberOfParticipants(entity.getNumberOfParticipants())
            .singlesCount(entity.getSinglesCount())
            .description(entity.getDescription())
            .bestAlbum(
                (entity.getBestAlbum() == null) ? null :
                    AlbumMapper.toDtoFromEntity(entity.getBestAlbum())
            )
            .albumsCount(entity.getAlbumsCount())
            .establishmentDate(entity.getEstablishmentDate())
            .studio(
                (entity.getStudio() == null) ? null :
                    StudioMapper.toDtoFromEntity(entity.getStudio())
            )
            .build();
    }
}
