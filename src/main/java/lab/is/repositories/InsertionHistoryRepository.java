package lab.is.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lab.is.bd.entities.InsertionHistory;

@Repository
public interface InsertionHistoryRepository extends JpaRepository<InsertionHistory, Long> {
    @Query("SELECT ih FROM InsertionHistory ih WHERE ih.user.id = :userId")
    Page<InsertionHistory> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
