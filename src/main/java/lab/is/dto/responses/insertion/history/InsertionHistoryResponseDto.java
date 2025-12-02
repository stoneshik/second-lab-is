package lab.is.dto.responses.insertion.history;

import java.time.LocalDateTime;

import lab.is.bd.entities.InsertionHistoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsertionHistoryResponseDto {
    private Long id;
    private LocalDateTime creationDate;
    private LocalDateTime endDate;
    private InsertionHistoryStatus status;
    private String login;
    private Long numberObjects;
}
