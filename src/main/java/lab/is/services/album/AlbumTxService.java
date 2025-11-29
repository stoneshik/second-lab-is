package lab.is.services.album;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.Album;
import lab.is.exceptions.NestedObjectNotFoundException;
import lab.is.repositories.AlbumRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class AlbumTxService {
    private final AlbumRepository albumRepository;

    @Transactional(readOnly = true)
    Album findByIdReturnsEntity(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(
                    () -> new NestedObjectNotFoundException(
                        String.format("Альбом с id: %s не найден", id)
                    )
                );
    }
}
