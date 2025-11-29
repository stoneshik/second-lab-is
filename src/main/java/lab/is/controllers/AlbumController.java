package lab.is.controllers;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lab.is.dto.requests.album.AlbumRequestCreateDto;
import lab.is.dto.requests.album.AlbumRequestUpdateDto;
import lab.is.dto.responses.albums.AlbumResponseDto;
import lab.is.dto.responses.albums.WrapperListAlbumResponseDto;
import lab.is.services.album.AlbumService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/albums")
@RequiredArgsConstructor
public class AlbumController {
    private static final String URI_RESOURCE = "/api/v1/albums";
    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<WrapperListAlbumResponseDto> getAll(
        @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(
            albumService.findAll(pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponseDto> getById(@PathVariable Long id) {
        AlbumResponseDto dto = albumService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid AlbumRequestCreateDto dto) {
        Long createdId = albumService.create(
            dto.getName(),
            dto.getLength()
        ).getId();
        URI location = URI.create(URI_RESOURCE + "/" + createdId);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @PathVariable Long id,
        @RequestBody @Valid AlbumRequestUpdateDto dto
    ) {
        albumService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
