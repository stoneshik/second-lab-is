package lab.is.util.musicband;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Album;
import lab.is.bd.entities.Coordinates;
import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.Studio;
import lab.is.dto.requests.album.AlbumRequestCreateDto;
import lab.is.dto.requests.coordinates.CoordinatesCreateRequestDto;
import lab.is.dto.requests.musicband.MusicBandRequestCreateDto;
import lab.is.dto.requests.studio.StudioRequestCreateDto;
import lab.is.exceptions.IncorrectDtoInRequestException;
import lab.is.services.album.AlbumService;
import lab.is.services.coordinates.CoordinatesService;
import lab.is.services.studio.StudioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicBandToEntityFromDtoCreateRequest {
    private final AlbumService albumService;
    private final CoordinatesService coordinatesService;
    private final StudioService studioService;

    @Transactional
    public MusicBand toEntityFromDto(MusicBandRequestCreateDto dto) {
        if (isCombinationInfoAboutNestedObjectsDtoIncorrect(
            dto.getCoordinates(),
            dto.getBestAlbum(),
            dto.getStudio(),
            dto.getCoordinatesId(),
            dto.getBestAlbumId(),
            dto.getStudioId()
        )) {
            throw new IncorrectDtoInRequestException("Ошибка в комбинации в информации о вложенных объектов");
        }
        Coordinates coordinates = findOrCreateCoordinatesEntityByMusicBandDto(
            dto.getCoordinates(),
            dto.getCoordinatesId()
        );
        Album bestAlbum = findOrCreateBestAlbumEntityByMusicBandDto(
            dto.getBestAlbum(),
            dto.getBestAlbumId()
        );
        Studio studio = extractOrCreateStudioEntityFromMusicBandDto(
            dto.getStudio(),
            dto.getStudioId()
        );
        return MusicBand.builder()
            .name(dto.getName())
            .coordinates(coordinates)
            .genre(dto.getGenre())
            .numberOfParticipants(dto.getNumberOfParticipants())
            .singlesCount(dto.getSinglesCount())
            .description(dto.getDescription())
            .bestAlbum(bestAlbum)
            .albumsCount(dto.getAlbumsCount())
            .establishmentDate(dto.getEstablishmentDate())
            .studio(studio)
            .build();
    }

    public boolean isCombinationInfoAboutNestedObjectsDtoIncorrect(
        CoordinatesCreateRequestDto coordinates,
        AlbumRequestCreateDto bestAlbum,
        StudioRequestCreateDto studio,
        Long coordinatesId,
        Long bestAlbumId,
        Long studioId
    ) {
        return (
            (coordinates == null && coordinatesId == null) ||
            (coordinates != null && coordinatesId != null) ||
            (bestAlbum != null && bestAlbumId != null) ||
            (studio != null && studioId != null)
        );
    }

    public Coordinates findOrCreateCoordinatesEntityByMusicBandDto(
        CoordinatesCreateRequestDto dto,
        Long id
    ) {
        if (dto != null) {
            return coordinatesService.create(
                dto.getX(),
                dto.getY()
            );
        }
        return coordinatesService.findByIdReturnsEntity(id);
    }

    public Album findOrCreateBestAlbumEntityByMusicBandDto(
        AlbumRequestCreateDto dto,
        Long id
    ) {
        if (dto == null && id == null) return null;
        if (dto != null) {
            return albumService.create(
                dto.getName(),
                dto.getLength()
            );
        }
        return albumService.findByIdReturnsEntity(id);
    }

    public Studio extractOrCreateStudioEntityFromMusicBandDto(
        StudioRequestCreateDto dto,
        Long id
    ) {
        if (dto == null && id == null) return null;
        if (dto != null) {
            return studioService.create(
                dto.getName(),
                dto.getAddress()
            );
        }
        return studioService.findByIdReturnsEntity(id);
    }
}
