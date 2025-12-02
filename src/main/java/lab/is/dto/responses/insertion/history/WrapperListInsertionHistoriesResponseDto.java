package lab.is.dto.responses.insertion.history;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WrapperListInsertionHistoriesResponseDto {
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
    private List<InsertionHistoryResponseDto> insertionHistories;
}
