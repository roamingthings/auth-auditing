package de.roamingthings.authaudit.authauditing.repository;

import de.roamingthings.authaudit.authauditing.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/03
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u.id FROM User u where u.username = :username")
    Optional<Long> findUserIdByUsername(@Param("username") String username);
}
