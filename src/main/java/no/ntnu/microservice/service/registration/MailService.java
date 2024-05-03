package no.ntnu.microservice.service.registration;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.ntnu.microservice.model.sharedmodels.user.User;

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
        mailMessage.setFrom("magnusgjerstad00@gmail.com");
        mailMessage.setText(constructEmailBody(user, token));

        mailSender.send(mailMessage);
    }

    private String constructEmailBody(User user, String token) {
        return String.format(
                "Hei %s %s,\n\n" +
                        "Velkommen til Sparesti! Vi er glade for å ha deg ombord. For å fullføre registreringen din og aktivere kontoen din, vennligst bekreft e-postadressen din ved å bruke følgende verifiseringstoken:\n\n"
                        +
                        "Verifiseringstoken: %s\n\n" +
                        "Skriv denne koden inn i vinduet på nettsiden for å fullføre registreringen din.\n\n" +
                        "Du kan også verdifisere deg ved å bruke en av disse linkene\n\n"
                        +
                        "Prod-miljø:\n" +
                        "https://sparesti.tech/login/verifyEmail/%s\n\n" +
                        "Lokal Docker-utvikling:\n" +
                        "http://127.0.0.1:80/login/verifyEmail/%s\n\n" +
                        "For å verdifisere bruker med microservice endepunkt, send en GET-forespørsel til:\n" +
                        "http://127.0.0.1:8111/auth/verify?token=%s\n\n" +
                        "Takk for at du valgte Sparesti! Vi ser frem til å hjelpe deg i å oppnå dine mål.\n\n" +
                        "Med vennlig hilsen,\n" +
                        "Sparesti Teamet",
                user.getFirstName(), user.getLastName(), token, token, token, token);
    }

    public void sendResetPasswordEmail(User user, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Reset your password!");
        mailMessage.setFrom("magnusgjerstad00@gmail.com");

        mailMessage.setText(constructPasswordResetEmailBody(user, token));

        mailSender.send(mailMessage);
    }

    public String constructPasswordResetEmailBody(User user, String token) {
        return String.format(
                "Hei %s %s,\n\n" +
                        "Vi har mottatt en forespørsel om å tilbakestille passordet ditt. Hvis du ikke har bedt om dette, kan du ignorere denne e-posten.\n\n"
                        +
                        "For å tilbakestille passordet ditt, vennligst bruk følgende token:\n\n" +
                        "Tilbakestillingstoken: %s\n\n" +
                        "For å tilbakestille passordet på forskjellige plattformer, vennligst bruk den relevante lenken nedenfor basert på ditt bruksmiljø:\n\n"
                        +
                        "Prod-miljø:\n" +
                        "https://sparesti.tech/login/resetPassword/%s\n\n" +
                        "Lokal Docker-utvikling:\n" +
                        "http://127.0.0.1:80/login/resetPassword/%s\n\n" +
                        "For å tilbakestille passordet med microservice endepunkt, send en POST-forespørsel til:\n" +
                        "http://127.0.0.1:8111/auth/resetPassword\n\n" +
                        "med følgende JSON-objekt:\n" +
                        "{\n" +
                        "    \"token\": \"fyllinntokenher\",\n" +
                        "    \"password\": \"nyttpassord\"\n" +
                        "}\n\n" +

                        "Hvis du har noen spørsmål, vennligst kontakt oss på grupperommet!\n\n" +
                        "Med vennlig hilsen,\n" +
                        "Gruppe 11",
                user.getFirstName(), user.getLastName(), token, token, token);
    }
}