package lab.is.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lab.is.bd.entities.InsertionHistory;

@Repository
public interface InsertionHistoryRepository extends JpaRepository<InsertionHistory, Long> {

}
