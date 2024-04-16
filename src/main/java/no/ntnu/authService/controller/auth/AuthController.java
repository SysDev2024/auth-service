package no.ntnu.authService.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.authService.model.DTO.auth.AuthResponse;
import no.ntnu.authService.model.DTO.auth.LoginRequest;
import no.ntnu.authService.model.DTO.auth.MessageResponse;
import no.ntnu.authService.model.DTO.auth.RegisterRequest;
import no.ntnu.authService.service.AuthService;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("Hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("Login attempt with username: {}", request.getUsername());

        return ResponseEntity.ok(authService.login(request));
    }

    // TODO Email validation
    // TODO make sure username is unique
    // Check password lenght
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<AuthResponse> verifyEmail(String token) {

        return ResponseEntity.ok(authService.verifyEmail(token));
    }

}
