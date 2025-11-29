package lab.is.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import lab.is.bd.entities.Nomination;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long>,
    JpaSpecificationExecutor<Nomination> {

    @Override
    @EntityGraph(attributePaths = {"musicBand"})
    Page<Nomination> findAll(Specification<Nomination> spec, Pageable pageable);
}
