package no.ntnu.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.microservice.model.sharedmodels.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

}
