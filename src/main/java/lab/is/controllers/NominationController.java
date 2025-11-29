package lab.is.controllers;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lab.is.bd.entities.MusicGenre;
import lab.is.dto.requests.nomination.NominationCreateRequestDto;
import lab.is.dto.responses.nomination.NominationResponseDto;
import lab.is.dto.responses.nomination.WrapperListNominationResponseDto;
import lab.is.services.nomination.NominationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/nominations")
@RequiredArgsConstructor
public class NominationController {
    private static final String URI_RESOURCE = "/api/v1/nominations";
    private final NominationService nominationService;

    @GetMapping
    public ResponseEntity<WrapperListNominationResponseDto> getAll(
        @RequestParam(required = false) Long musicBandId,
        @RequestParam(required = false) MusicGenre genre,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime nominatedFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime nominatedTo,
        @PageableDefault(page = 0, size = 10, sort = "nominatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
            nominationService.findAll(
                musicBandId,
                genre,
                nominatedFrom,
                nominatedTo,
                pageable
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NominationResponseDto> getById(@PathVariable Long id) {
        NominationResponseDto dto = nominationService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid NominationCreateRequestDto dto) {
        Long createdId = nominationService.create(
            dto.getMusicBandId(),
            dto.getMusicGenre()
        ).getId();
        URI location = URI.create(URI_RESOURCE + "/" + createdId);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        nominationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
