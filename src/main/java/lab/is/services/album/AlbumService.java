package lab.is.services.album;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Album;
import lab.is.dto.requests.album.AlbumRequestUpdateDto;
import lab.is.dto.responses.albums.AlbumResponseDto;
import lab.is.dto.responses.albums.WrapperListAlbumResponseDto;
import lab.is.exceptions.NestedObjectIsUsedException;
import lab.is.repositories.AlbumRepository;
import lab.is.util.AlbumMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumTxService albumTxService;

    @Transactional(readOnly = true)
    public WrapperListAlbumResponseDto findAll(Pageable pageable) {
        Page<Album> page = albumRepository.findAll(pageable);
        List<AlbumResponseDto> albumResponseDtos = new ArrayList<>();

        page.forEach(album ->
            albumResponseDtos.add(
                AlbumMapper.toDtoFromEntity(album)
            )
        );

        return WrapperListAlbumResponseDto.builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getNumberOfElements())
            .albums(albumResponseDtos)
            .build();
    }

    @Transactional(readOnly = true)
    public AlbumResponseDto findById(Long id) {
        Album album = albumTxService.findByIdReturnsEntity(id);
        return AlbumMapper.toDtoFromEntity(album);
    }

    @Transactional
    public Album create(String name, int length) {
        Album album = Album.builder()
            .name(name)
            .length(length)
            .build();
        Album savedAlbum = albumRepository.save(album);
        albumRepository.flush();
        return savedAlbum;
    }

    @Transactional
    public Album update(long id, AlbumRequestUpdateDto dto) {
        Album album = albumTxService.findByIdReturnsEntity(id);
        Album updatedAlbum = album.toBuilder()
            .name(dto.getName())
            .length(dto.getLength())
            .build();
        Album savedAlbum = albumRepository.save(updatedAlbum);
        albumRepository.flush();
        return savedAlbum;
    }

    @Transactional
    public void delete(Long id) {
        Album album = albumTxService.findByIdReturnsEntity(id);
        if (isUsedNestedObject(album)) {
            throw new NestedObjectIsUsedException(
                String.format(
                    "Альбом %s с id: %s не может быть удален, так как связан с другими объектами",
                    album.getName(),
                    id
                )
            );
        }
        albumRepository.delete(album);
    }

    public Album findByIdReturnsEntity(Long id) {
        return albumTxService.findByIdReturnsEntity(id);
    }

    private boolean isUsedNestedObject(Album album) {
        return !album.getMusicBands().isEmpty();
    }
}
