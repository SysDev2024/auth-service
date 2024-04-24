package no.ntnu.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.microservice.model.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
}