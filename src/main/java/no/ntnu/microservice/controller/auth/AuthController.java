package no.ntnu.microservice.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import no.ntnu.microservice.model.DTO.auth.AuthResponse;
import no.ntnu.microservice.model.DTO.auth.LoginRequest;
import no.ntnu.microservice.model.DTO.auth.MessageResponse;
import no.ntnu.microservice.model.DTO.auth.RegisterRequest;
import no.ntnu.microservice.service.AuthService;
import no.ntnu.microservice.model.DTO.EmailDTO;
import no.ntnu.microservice.model.DTO.ResetEmailDTO;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Check if a service is running", description = "Returns a simple greeting")
    @GetMapping("heartbeat")
    public String hello() {
        return "I am alive";
    }

    @Operation(summary = "Log in a user and generate a JWT token", description = "Log in a user and generate a JWT token. The token is used to authenticate the user in the other services and is authenticated in the API Gateway", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Token", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "400", description = "User not found, wrong username or password")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception ex) {
            AuthResponse errorResponse = new AuthResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Register a new user", description = "Register a new user. The user will receive an email with a link to verify the email address", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "User registered. Check your email to validate account", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @ApiResponse(responseCode = "400", description = "Username already exists")
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (Exception ex) {
            MessageResponse errorResponse = new MessageResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Verify an email address", description = "Verify an email address using a token sent to the email address", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Email verified", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "400", description = "Token not found")
    @GetMapping("/verify")
    public ResponseEntity<AuthResponse> verifyEmail(String token) {
        try {
            return ResponseEntity.ok(authService.verifyEmail(token));
        } catch (Exception ex) {
            AuthResponse errorResponse = new AuthResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Boolean> refreshToken(@RequestBody String token) {
        return ResponseEntity.ok(true);

    }

    @Operation(summary = "Send a password reset email", description = "Send a password reset email to the user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Check your email to reset password", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @ApiResponse(responseCode = "400", description = "User not found")
    @PostMapping("/forgotPassword")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody EmailDTO email) {
        try {
            return ResponseEntity.ok(authService.forgotPassword(email));
        } catch (Exception ex) {
            MessageResponse errorResponse = new MessageResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Reset a password", description = "Reset a password using a token sent to the email address", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Password reset", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "400", description = "Token not found")
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
