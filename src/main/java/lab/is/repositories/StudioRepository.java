package lab.is.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lab.is.bd.entities.Studio;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Long> {
}
