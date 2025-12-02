package lab.is.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lab.is.dto.responses.coordinates.WrapperListCoordinatesResponseDto;
import lab.is.services.insertion.history.InsertionHistoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/insertion/histories")
@RequiredArgsConstructor
public class InsertionHistoryController {
    private final InsertionHistoryService insertionHistoryService;

    @GetMapping
    public ResponseEntity<WrapperListCoordinatesResponseDto> getAll(
        @RequestParam(required = true) Long userId,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        if (userId == null) {
            return ResponseEntity.ok(
                insertionHistoryService.findAll(pageable)
            );
        }
        return ResponseEntity.ok(
            insertionHistoryService.findAllByUserId(pageable, userId)
        );
    }
}
