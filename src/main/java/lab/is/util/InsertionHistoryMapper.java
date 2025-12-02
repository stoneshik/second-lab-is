package lab.is.util;

import lab.is.bd.entities.InsertionHistory;
import lab.is.dto.responses.insertion.history.InsertionHistoryResponseDto;

public class InsertionHistoryMapper {
    private InsertionHistoryMapper() {}

    public static InsertionHistoryResponseDto toDtoFromEntity(InsertionHistory entity) {
        return InsertionHistoryResponseDto.builder()
            .id(entity.getId())
            .creationDate(entity.getCreationDate())
            .endDate(entity.getEndDate())
            .status(entity.getStatus())
            .login(entity.getUser().getLogin())
            .numberObjects(entity.getNumberObjects())
            .build();
    }
}
