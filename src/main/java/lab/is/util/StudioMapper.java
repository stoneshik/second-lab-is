package lab.is.util;

import lab.is.bd.entities.Studio;
import lab.is.dto.requests.studio.StudioRequestCreateDto;
import lab.is.dto.requests.studio.StudioRequestUpdateDto;
import lab.is.dto.responses.studios.StudioResponseDto;

public class StudioMapper {
    private StudioMapper() {}

    public static Studio toEntityFromDto(StudioRequestCreateDto dto) {
        return Studio.builder()
            .name(dto.getName())
            .address(dto.getAddress())
            .build();
    }

    public static Studio toEntityFromDto(StudioRequestUpdateDto dto) {
        return Studio.builder()
            .name(dto.getName())
            .address(dto.getAddress())
            .build();
    }

    public static StudioResponseDto toDtoFromEntity(Studio entity) {
        return StudioResponseDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .address(entity.getAddress())
            .build();
    }
}
