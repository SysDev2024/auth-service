package no.ntnu.authService.service;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.authService.model.DTO.auth.AuthResponse;
import no.ntnu.authService.model.DTO.auth.LoginRequest;
import no.ntnu.authService.model.DTO.auth.MessageResponse;
import no.ntnu.authService.model.DTO.auth.RegisterRequest;
import no.ntnu.authService.model.User.Role;
import no.ntnu.authService.model.User.User;
import no.ntnu.authService.repository.UserRepository;
import no.ntnu.authService.security.JwtService;
import no.ntnu.authService.service.registration.EmailVerificationTokenService;
import no.ntnu.authService.service.registration.MailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final MailService mailService;

    private final EmailVerificationTokenService emailverificationTokenService;

    public MessageResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.USER)
                .enabled(false)
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        String token = emailverificationTokenService.generateVerificationToken(user);
        mailService.sendVerificationEmail(user, token);

        return MessageResponse.builder().message("User registered. Check your email to validate account").build();

        // var jwtToken = jwtService.generateToken(user);
        // return AuthResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public AuthResponse verifyEmail(String token) {

        var verificationToken = emailverificationTokenService.getVerificationToken(token);

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        var user = userRepository.findById(verificationToken.getUser().getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Check if the user is already enabled
        if (user.isEnabled()) {
            throw new IllegalStateException("Email is already verified");
        }

        user.setEnabled(true);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
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
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("User not found, wrong username or password"));
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

}
