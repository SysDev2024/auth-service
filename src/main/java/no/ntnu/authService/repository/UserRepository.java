package no.ntnu.authService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.authService.model.sharedmodels.User.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

}
