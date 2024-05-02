package no.ntnu.microservice.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import no.ntnu.microservice.model.DTO.auth.AuthResponse;
import no.ntnu.microservice.model.DTO.auth.LoginRequest;
import no.ntnu.microservice.model.DTO.auth.MessageResponse;
import no.ntnu.microservice.model.DTO.auth.RegisterRequest;
import no.ntnu.microservice.service.AuthService;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
