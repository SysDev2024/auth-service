package no.ntnu.microservice.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.microservice.model.DTO.auth.AuthResponse;
import no.ntnu.microservice.model.DTO.auth.LoginRequest;
import no.ntnu.microservice.model.DTO.auth.MessageResponse;
import no.ntnu.microservice.model.DTO.auth.RegisterRequest;
import no.ntnu.microservice.security.JwtService;
import no.ntnu.microservice.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.core.userdetails.UserDetailsService;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("Hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception ex) {
            AuthResponse errorResponse = new AuthResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // TODO Email validation
    // TODO make sure username is unique
    // Check password lenght
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (Exception ex) {
            MessageResponse errorResponse = new MessageResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<AuthResponse> verifyEmail(String token) {

        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {

        return ResponseEntity.ok(true);

    }

}
