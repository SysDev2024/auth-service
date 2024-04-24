package no.ntnu.authService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.authService.model.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
}