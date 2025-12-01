package lab.is.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lab.is.security.bd.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Query("SELECT rt FROM refreshtoken rt WHERE rt.user.id = :userId")
    Optional<RefreshToken> findRefreshTokenByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM refreshtoken rt WHERE rt.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
