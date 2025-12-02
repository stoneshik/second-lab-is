package lab.is.services.insertion.history;

import org.springframework.stereotype.Service;

import lab.is.repositories.InsertionHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsertionHistoryService {
    private final InsertionHistoryRepository insertionHistoryRepository;
}
