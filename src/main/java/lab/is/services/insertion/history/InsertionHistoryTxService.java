package lab.is.services.insertion.history;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lab.is.bd.entities.InsertionHistory;
import lab.is.exceptions.NotFoundException;
import lab.is.repositories.InsertionHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsertionHistoryTxService {
    private final InsertionHistoryRepository insertionHistoryRepository;

    @Transactional(readOnly = true)
    public InsertionHistory findById(long id) {
        return insertionHistoryRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException(
                    String.format("История вставки с id: %s не найдена", id)
                )
            );
    }
}
