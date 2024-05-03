package no.ntnu.microservice.service.registration;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.microservice.model.EmailVerificationToken;
import no.ntnu.microservice.model.sharedmodels.user.User;
import no.ntnu.microservice.repository.EmailVerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    private final EmailVerificationTokenRepository verificationTokenRepository;

    @Override
    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder().token(token).user(user).build();
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        saveVerificationToken(verificationToken);
        return token;
    }

    @Override
    public void saveVerificationToken(EmailVerificationToken token) {
        verificationTokenRepository.save(token);
    }

    // TODO add custom exeption

    @Override
    public EmailVerificationToken getVerificationToken(String token) {
        Optional<EmailVerificationToken> tokenOptional = verificationTokenRepository.findByToken(token);
        return tokenOptional.orElseThrow(() -> new IllegalArgumentException("Token not found"));

    };

}
