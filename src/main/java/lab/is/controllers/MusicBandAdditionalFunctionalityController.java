package lab.is.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lab.is.dto.responses.musicband.MusicBandResponseDto;
import lab.is.services.musicband.MusicBandAdditionalFunctionalityService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/music-bands")
@RequiredArgsConstructor
public class MusicBandAdditionalFunctionalityController {
    private final MusicBandAdditionalFunctionalityService musicBandAdditionalFunctionalityService;

    @DeleteMapping("/by-establishment")
    public ResponseEntity<Void> deleteOneByEstablishmentDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        boolean deleted = musicBandAdditionalFunctionalityService.deleteOneByEstablishmentDate(date);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/min-id")
    public ResponseEntity<MusicBandResponseDto> getOneWithMinId() {
        MusicBandResponseDto dto = musicBandAdditionalFunctionalityService.findOneWithMinId();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/after-establishment")
    public ResponseEntity<List<MusicBandResponseDto>> getBandsAfterEstablishment(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(
            musicBandAdditionalFunctionalityService.findAllWithEstablishmentDateAfter(date)
        );
    }

    @PostMapping("/{id}/singles")
    public ResponseEntity<Void> addSingleToBand(@PathVariable Long id) {
        musicBandAdditionalFunctionalityService.addSingleToBand(id);
        return ResponseEntity.noContent().build();
    }
}
