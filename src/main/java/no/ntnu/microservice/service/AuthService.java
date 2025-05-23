package no.ntnu.microservice.service;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import no.ntnu.microservice.model.DTO.auth.AuthResponse;
import no.ntnu.microservice.model.DTO.auth.LoginRequest;
import no.ntnu.microservice.model.DTO.auth.MessageResponse;
import no.ntnu.microservice.model.DTO.auth.RegisterRequest;
import no.ntnu.microservice.model.sharedmodels.user.Role;
import no.ntnu.microservice.model.sharedmodels.user.User;
import no.ntnu.microservice.repository.UserRepository;
import no.ntnu.microservice.security.JwtService;
import no.ntnu.microservice.service.registration.EmailVerificationTokenService;
import no.ntnu.microservice.service.registration.MailService;
import no.ntnu.microservice.model.DTO.EmailDTO;
import no.ntnu.microservice.model.DTO.ResetEmailDTO;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final MailService mailService;

    private final EmailVerificationTokenService emailverificationTokenService;

    public MessageResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.USER)
                .enabled(false)
                .firstLogin(true)
                .hasConnectedBankAccount(false)
                .hasCustomizedProfile(false)
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        String token = emailverificationTokenService.generateVerificationToken(user);
        mailService.sendVerificationEmail(user, token);

        return MessageResponse.builder().message("User registered. Check your email to validate account").build();
    }

    @Transactional
    public AuthResponse verifyEmail(String token) {

        var verificationToken = emailverificationTokenService.getVerificationToken(token);

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        User user = userRepository.findById(verificationToken.getUser().getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Check if the user is already enabled
        if (user.isEnabled()) {
            throw new IllegalStateException("Email is already verified");
        }

        user.setEnabled(true);
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();

    }

    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new IllegalArgumentException("User not found, wrong username or password");
        }
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("User not found, wrong username or password"));
        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public MessageResponse forgotPassword(EmailDTO email) {

        User user = userRepository.findByEmail(email.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = emailverificationTokenService.generateVerificationToken(user);
        mailService.sendResetPasswordEmail(user, token);

        return MessageResponse.builder().message("Check your email to reset password").build();
    }

    public AuthResponse resetPassword(ResetEmailDTO resetEmailDTO) {

        var verificationToken = emailverificationTokenService.getVerificationToken(resetEmailDTO.getToken());

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        User user = userRepository.findById(verificationToken.getUser().getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setPassword(passwordEncoder.encode(resetEmailDTO.getPassword()));
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

}
