package no.ntnu.authService.service.registration;

import no.ntnu.authService.model.User.EmailVerificationToken;
import no.ntnu.authService.model.User.User;

public interface EmailVerificationTokenService {
    String generateVerificationToken(User user);

    void saveVerificationToken(EmailVerificationToken token);

    EmailVerificationToken getVerificationToken(String token);

}