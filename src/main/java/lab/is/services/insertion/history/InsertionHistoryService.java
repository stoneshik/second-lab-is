package lab.is.services.insertion.history;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.InsertionHistory;
import lab.is.dto.responses.insertion.history.InsertionHistoryResponseDto;
import lab.is.dto.responses.insertion.history.WrapperListInsertionHistoriesResponseDto;
import lab.is.repositories.InsertionHistoryRepository;
import lab.is.security.services.UserService;
import lab.is.util.InsertionHistoryMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsertionHistoryService {
    private final InsertionHistoryRepository insertionHistoryRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public WrapperListInsertionHistoriesResponseDto findAll(Pageable pageable) {
        Page<InsertionHistory> page = insertionHistoryRepository.findAll(pageable);
        List<InsertionHistoryResponseDto> insertionHistoryResponseDtos = new ArrayList<>();

        page.forEach(insertionHistory ->
            insertionHistoryResponseDtos.add(
                InsertionHistoryMapper.toDtoFromEntity(insertionHistory)
            )
        );

        return WrapperListInsertionHistoriesResponseDto.builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getNumberOfElements())
            .insertionHistories(insertionHistoryResponseDtos)
            .build();
    }

    @Transactional(readOnly = true)
    public WrapperListInsertionHistoriesResponseDto findAllByUserId(Pageable pageable, Long userId) {
        userService.loadUserById(userId);
        Page<InsertionHistory> page = insertionHistoryRepository.findAllByUserId(userId, pageable);
        List<InsertionHistoryResponseDto> insertionHistoryResponseDtos = new ArrayList<>();

        page.forEach(insertionHistory ->
            insertionHistoryResponseDtos.add(
                InsertionHistoryMapper.toDtoFromEntity(insertionHistory)
            )
        );

        return WrapperListInsertionHistoriesResponseDto.builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getNumberOfElements())
            .insertionHistories(insertionHistoryResponseDtos)
            .build();
    }
}
