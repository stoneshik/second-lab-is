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
import lab.is.dto.requests.coordinates.CoordinatesCreateRequestDto;
import lab.is.dto.requests.coordinates.CoordinatesUpdateRequestDto;
import lab.is.dto.responses.coordinates.CoordinatesResponseDto;
import lab.is.dto.responses.coordinates.WrapperListCoordinatesResponseDto;
import lab.is.services.coordinates.CoordinatesService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coordinates")
@RequiredArgsConstructor
public class CoordinatesController {
    private static final String URI_RESOURCE = "/api/v1/coordinates";
    private final CoordinatesService coordinatesService;

    @GetMapping
    public ResponseEntity<WrapperListCoordinatesResponseDto> getAll(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(
            coordinatesService.findAll(pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoordinatesResponseDto> getById(@PathVariable Long id) {
        CoordinatesResponseDto dto = coordinatesService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CoordinatesCreateRequestDto dto) {
        Long createdId = coordinatesService.create(
            dto.getX(),
            dto.getY()
        ).getId();
        URI location = URI.create(URI_RESOURCE + "/" + createdId);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @PathVariable Long id,
        @RequestBody @Valid CoordinatesUpdateRequestDto dto
    ) {
        coordinatesService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coordinatesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
