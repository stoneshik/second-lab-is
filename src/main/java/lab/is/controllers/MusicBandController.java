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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lab.is.bd.entities.MusicGenre;
import lab.is.dto.requests.musicband.MusicBandRequestCreateDto;
import lab.is.dto.requests.musicband.MusicBandRequestUpdateDto;
import lab.is.dto.responses.musicband.MusicBandResponseDto;
import lab.is.dto.responses.musicband.WrapperListMusicBandResponseDto;
import lab.is.services.musicband.MusicBandService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/music-bands")
@RequiredArgsConstructor
public class MusicBandController {
    private static final String URI_RESOURCE = "/api/v1/music-bands";
    private final MusicBandService musicBandService;

    @GetMapping
    public ResponseEntity<WrapperListMusicBandResponseDto> getAll(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) MusicGenre genre,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String bestAlbumName,
        @RequestParam(required = false) String studioName,
        @RequestParam(required = false) String studioAddress,
        @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(
            musicBandService.findAll(
                name,
                genre,
                description,
                bestAlbumName,
                studioName,
                studioAddress,
                pageable
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicBandResponseDto> getById(@PathVariable Long id) {
        MusicBandResponseDto dto = musicBandService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid MusicBandRequestCreateDto dto) {
        Long createdId = musicBandService.create(dto).getId();
        URI location = URI.create(URI_RESOURCE + "/" + createdId);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @PathVariable Long id,
        @RequestBody @Valid MusicBandRequestUpdateDto dto
    ) {
        musicBandService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        musicBandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
