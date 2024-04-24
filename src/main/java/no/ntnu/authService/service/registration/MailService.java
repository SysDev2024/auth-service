package no.ntnu.authService.service.registration;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.authService.model.sharedmodels.User.User;

import org.springframework.mail.SimpleMailMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    private static final String siteURL = "http://localhost:8080/api/v1/auth";

    public void sendVerificationEmail(User user, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete your registration!");
        mailMessage.setFrom("noreply@your-domain.com");
        mailMessage.setText(constructEmailBody(user, token));

        mailSender.send(mailMessage);
    }

    private String constructEmailBody(User user, String token) {
        return String.format(
                "Hei %s %s,\n\n" +
                        "Velkommen til Sparesti! Vi er glade for å ha deg ombord. For å fullføre registreringen din og aktivere kontoen din, vennligst bekreft e-postadressen din ved å bruke følgende verifiseringstoken:\n\n"
                        +
                        "Verifiseringstoken: %s\n\n" +
                        "Vennligst kopier og lim inn denne tokenen på verifiseringssiden som du kan nå gjennom følgende link:\n%s/user/verify\n\n"
                        +
                        "Takk for at du valgte Sparesti! Vi ser frem til å hjelpe deg i å oppnå dine mål.\n\n" +
                        "Med vennlig hilsen,\n" +
                        "Sparesti Teamet",
                user.getFirstName(), user.getLastName(), token, siteURL);
    }

}
