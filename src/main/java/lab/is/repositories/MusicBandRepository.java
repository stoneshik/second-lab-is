package lab.is.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import lab.is.bd.entities.MusicBand;

@Repository
public interface MusicBandRepository extends JpaRepository<MusicBand, Long>,
    JpaSpecificationExecutor<MusicBand> {
    Optional<MusicBand> findFirstByEstablishmentDate(LocalDate establishmentDate);

    Optional<MusicBand> findFirstByOrderByIdAsc();

    List<MusicBand> findByEstablishmentDateAfter(LocalDate date);

    @Override
    @EntityGraph(attributePaths = {"coordinates", "bestAlbum", "studio"})
    Page<MusicBand> findAll(Specification<MusicBand> specification, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"coordinates", "bestAlbum", "studio"})
    Optional<MusicBand> findById(Long id);
}
