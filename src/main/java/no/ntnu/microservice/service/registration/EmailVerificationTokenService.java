package no.ntnu.microservice.service.registration;

import no.ntnu.microservice.model.EmailVerificationToken;
import no.ntnu.microservice.model.sharedmodels.user.User;

public interface EmailVerificationTokenService {
    String generateVerificationToken(User user);

    void saveVerificationToken(EmailVerificationToken token);

    EmailVerificationToken getVerificationToken(String token);

}