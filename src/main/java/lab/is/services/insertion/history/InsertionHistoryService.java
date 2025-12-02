package lab.is.services.insertion.history;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.InsertionHistory;
import lab.is.bd.entities.InsertionHistoryStatus;
import lab.is.dto.responses.insertion.history.InsertionHistoryResponseDto;
import lab.is.dto.responses.insertion.history.WrapperListInsertionHistoriesResponseDto;
import lab.is.repositories.InsertionHistoryRepository;
import lab.is.security.bd.entities.User;
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

    @Transactional
    public InsertionHistory create(Long userId) {
        User user = userService.loadUserById(userId);
        InsertionHistory insertionHistory = InsertionHistory.builder()
            .status(InsertionHistoryStatus.PENDING)
            .user(user)
            .build();
        insertionHistoryRepository.save(insertionHistory);
        insertionHistoryRepository.flush();
        return insertionHistory;
    }

    @Transactional
    public InsertionHistory updateStatusToSuccess(InsertionHistory insertionHistory, Long numberObjects) {
        InsertionHistory updatedInsertionHistory = insertionHistory.toBuilder()
            .endDate(LocalDateTime.now())
            .status(InsertionHistoryStatus.SUCCESS)
            .numberObjects(numberObjects)
            .build();
        insertionHistoryRepository.save(updatedInsertionHistory);
        insertionHistoryRepository.flush();
        return updatedInsertionHistory;
    }

    @Transactional
    public InsertionHistory updateStatusToFailed(InsertionHistory insertionHistory) {
        InsertionHistory updatedInsertionHistory = insertionHistory.toBuilder()
            .endDate(LocalDateTime.now())
            .status(InsertionHistoryStatus.FAILED)
            .build();
        insertionHistoryRepository.save(updatedInsertionHistory);
        insertionHistoryRepository.flush();
        return updatedInsertionHistory;
    }
}
