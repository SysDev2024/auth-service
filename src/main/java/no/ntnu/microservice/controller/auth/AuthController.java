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
import no.ntnu.microservice.model.DTO.EmailDTO;
import no.ntnu.microservice.model.DTO.ResetEmailDTO;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (Exception ex) {
            MessageResponse errorResponse = new MessageResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<AuthResponse> verifyEmail(String token) {

        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Boolean> refreshToken(@RequestBody String token) {
        return ResponseEntity.ok(true);

    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody EmailDTO email) {
        try {
            return ResponseEntity.ok(authService.forgotPassword(email));
        } catch (Exception ex) {
            MessageResponse errorResponse = new MessageResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetEmailDTO resetEmailDTO) {
        try {
            return ResponseEntity.ok(authService.resetPassword(resetEmailDTO));
        } catch (Exception ex) {
            AuthResponse errorResponse = new AuthResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}
