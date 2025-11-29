package lab.is.security.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lab.is.security.bd.entities.RoleEnum;
import lab.is.security.bd.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findById(Long id);
    Boolean existsByLogin(String login);
    List<User> findAllByRolesName(RoleEnum role);
}
