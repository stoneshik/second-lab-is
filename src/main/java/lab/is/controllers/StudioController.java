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
import lab.is.dto.requests.studio.StudioRequestCreateDto;
import lab.is.dto.requests.studio.StudioRequestUpdateDto;
import lab.is.dto.responses.studios.StudioResponseDto;
import lab.is.dto.responses.studios.WrapperListStudioResponseDto;
import lab.is.services.studio.StudioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/studios")
@RequiredArgsConstructor
public class StudioController {
    private static final String URI_RESOURCE = "/api/v1/studios";
    private final StudioService studioService;

    @GetMapping
    public ResponseEntity<WrapperListStudioResponseDto> getAll(
        @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(
            studioService.findAll(pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudioResponseDto> getById(@PathVariable Long id) {
        StudioResponseDto dto = studioService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid StudioRequestCreateDto dto) {
        Long createdId = studioService.create(
            dto.getName(),
            dto.getAddress()
        ).getId();
        URI location = URI.create(URI_RESOURCE + "/" + createdId);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @PathVariable Long id,
        @RequestBody @Valid StudioRequestUpdateDto dto
    ) {
        studioService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
