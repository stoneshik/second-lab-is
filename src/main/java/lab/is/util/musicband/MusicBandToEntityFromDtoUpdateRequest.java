package lab.is.util.musicband;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Album;
import lab.is.bd.entities.Coordinates;
import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.Studio;
import lab.is.dto.requests.album.AlbumRequestUpdateDto;
import lab.is.dto.requests.coordinates.CoordinatesUpdateRequestDto;
import lab.is.dto.requests.musicband.MusicBandRequestUpdateDto;
import lab.is.dto.requests.studio.StudioRequestUpdateDto;
import lab.is.exceptions.IncorrectDtoInRequestException;
import lab.is.services.album.AlbumService;
import lab.is.services.coordinates.CoordinatesService;
import lab.is.services.studio.StudioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicBandToEntityFromDtoUpdateRequest {
    private final AlbumService albumService;
    private final CoordinatesService coordinatesService;
    private final StudioService studioService;

    @Transactional
    public MusicBand toEntityFromDto(
        MusicBandRequestUpdateDto dto,
        MusicBand foundMusicBand
    ) {
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
        Coordinates coordinates = createOrFindCoordinatesEntityByMusicBandDto(
            foundMusicBand,
            dto.getCoordinates(),
            dto.getCoordinatesId()
        );
        Album bestAlbum = createOrFindBestAlbumEntityByMusicBandDto(
            foundMusicBand,
            dto.getBestAlbum(),
            dto.getBestAlbumId()
        );
        Studio studio = createOrFindStudioEntityByMusicBandDto(
            foundMusicBand,
            dto.getStudio(),
            dto.getStudioId()
        );
        return foundMusicBand.toBuilder()
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

    private boolean isCombinationInfoAboutNestedObjectsDtoIncorrect(
        CoordinatesUpdateRequestDto coordinates,
        AlbumRequestUpdateDto bestAlbum,
        StudioRequestUpdateDto studio,
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

    private Coordinates createOrFindCoordinatesEntityByMusicBandDto(
        MusicBand foundMusicBand,
        CoordinatesUpdateRequestDto coordinatesDto,
        Long coordinatesId
    ) {
        if (coordinatesDto != null) {
            return createCoordinatesEntityByMusicBandDto(foundMusicBand, coordinatesDto);
        }
        return findCoordinatesEntityByMusicBandDto(foundMusicBand, coordinatesId);
    }

    private Album createOrFindBestAlbumEntityByMusicBandDto(
        MusicBand foundMusicBand,
        AlbumRequestUpdateDto albumDto,
        Long albumId
    ) {
        if (albumDto == null && albumId == null) return null;
        if (albumDto != null) {
            return createBestAlbumEntityByMusicBandDto(foundMusicBand, albumDto);
        }
        return findBestAlbumEntityByMusicBandDto(foundMusicBand, albumId);
    }

    private Studio createOrFindStudioEntityByMusicBandDto(
        MusicBand foundMusicBand,
        StudioRequestUpdateDto studioDto,
        Long studioId
    ) {
        if (studioDto == null && studioId == null) return null;
        if (studioDto != null) {
            return createStudioEntityByMusicBandDto(foundMusicBand, studioDto);
        }
        return findStudioEntityByMusicBandDto(foundMusicBand, studioId);
    }

    private Coordinates createCoordinatesEntityByMusicBandDto(
        MusicBand foundMusicBand,
        CoordinatesUpdateRequestDto coordinatesDto
    ) {
        Coordinates createdCoordinates = coordinatesService.create(
            coordinatesDto.getX(),
            coordinatesDto.getY()
        );
        updateOneToManyConnection(
            foundMusicBand,
            foundMusicBand.getCoordinates(),
            createdCoordinates
        );
        return createdCoordinates;
    }

    private Coordinates findCoordinatesEntityByMusicBandDto(MusicBand foundMusicBand, Long coordinatesId) {
        Coordinates foundMusicBandCoordinates = foundMusicBand.getCoordinates();
        Long foundMusicBandCoordinatesId = foundMusicBandCoordinates.getId();

        Coordinates foundCoordinates = coordinatesService.findByIdReturnsEntity(coordinatesId);
        if (!foundMusicBandCoordinatesId.equals(foundCoordinates.getId())) {
            updateOneToManyConnection(
                foundMusicBand,
                foundMusicBandCoordinates,
                foundCoordinates
            );
        }
        return foundCoordinates;
    }

    private Album createBestAlbumEntityByMusicBandDto(
        MusicBand foundMusicBand,
        AlbumRequestUpdateDto albumDto
    ) {
        Album createdAlbum = albumService.create(
            albumDto.getName(),
            albumDto.getLength()
        );
        updateOneToManyConnection(
            foundMusicBand,
            foundMusicBand.getBestAlbum(),
            createdAlbum
        );
        return createdAlbum;
    }

    private Album findBestAlbumEntityByMusicBandDto(MusicBand foundMusicBand, Long albumId) {
        Album foundMusicBandBestAlbum = foundMusicBand.getBestAlbum();
        Long foundMusicBandAlbumId = foundMusicBandBestAlbum.getId();

        Album foundAlbum = albumService.findByIdReturnsEntity(albumId);
        if (!foundMusicBandAlbumId.equals(foundAlbum.getId())) {
            updateOneToManyConnection(
                foundMusicBand,
                foundMusicBandBestAlbum,
                foundAlbum
            );
        }
        return foundAlbum;
    }

    private Studio createStudioEntityByMusicBandDto(
        MusicBand foundMusicBand,
        StudioRequestUpdateDto studioDto
    ) {
        Studio createdStudio = studioService.create(
            studioDto.getName(),
            studioDto.getAddress()
        );
        updateOneToManyConnection(
            foundMusicBand,
            foundMusicBand.getStudio(),
            createdStudio
        );
        return createdStudio;
    }

    private Studio findStudioEntityByMusicBandDto(MusicBand foundMusicBand, Long studioId) {
        Studio foundMusicBandStudio = foundMusicBand.getStudio();
        Long foundMusicBandStudioId = foundMusicBandStudio.getId();

        Studio foundStudio = studioService.findByIdReturnsEntity(studioId);
        if (!foundMusicBandStudioId.equals(foundStudio.getId())) {
            updateOneToManyConnection(
                foundMusicBand,
                foundMusicBandStudio,
                foundStudio
            );
        }
        return foundStudio;
    }

    private void updateOneToManyConnection(
        MusicBand musicBand,
        Coordinates oldCoordinates,
        Coordinates newCoordinates
    ) {
        oldCoordinates.removeMusicBand(musicBand);
        newCoordinates.addMusicBand(musicBand);
    }

    private void updateOneToManyConnection(
        MusicBand musicBand,
        Album oldAlbum,
        Album newAlbum
    ) {
        oldAlbum.removeMusicBand(musicBand);
        newAlbum.addMusicBand(musicBand);
    }

    private void updateOneToManyConnection(
        MusicBand musicBand,
        Studio oldStudio,
        Studio newStudio
    ) {
        oldStudio.removeMusicBand(musicBand);
        newStudio.addMusicBand(musicBand);
    }
}
