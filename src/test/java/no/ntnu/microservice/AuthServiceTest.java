package no.ntnu.microservice;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import no.ntnu.microservice.model.DTO.EmailDTO;
import no.ntnu.microservice.model.DTO.ResetEmailDTO;
import no.ntnu.microservice.model.DTO.auth.*;
import no.ntnu.microservice.model.DTO.auth.AuthResponse;
import no.ntnu.microservice.model.DTO.auth.LoginRequest;
import no.ntnu.microservice.model.DTO.auth.MessageResponse;
import no.ntnu.microservice.model.DTO.auth.RegisterRequest;
import no.ntnu.microservice.model.EmailVerificationToken;
import no.ntnu.microservice.model.sharedmodels.user.Option;
import no.ntnu.microservice.model.sharedmodels.user.Role;
import no.ntnu.microservice.model.sharedmodels.user.User;
import no.ntnu.microservice.repository.UserRepository;
import no.ntnu.microservice.security.JwtService;
import no.ntnu.microservice.service.AuthService;
import no.ntnu.microservice.service.registration.EmailVerificationTokenService;
import no.ntnu.microservice.service.registration.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class AuthServiceTest {
  private static final String api = "/auth";

  static UUID userId = UUID.fromString("8c7e44b4-9ec8-4b69-ae4c-9ef793eb8dcd");

  @Autowired private MockMvc mockMvc;
  @MockBean private UserRepository userRepository;
  @MockBean private AuthenticationManager authenticationManager;
  @MockBean private EmailVerificationTokenService emailVerificationTokenService;
  @MockBean private MailService mailService;
  @MockBean private PasswordEncoder passwordEncoder;
  @MockBean private JwtService jwtService;
  @Autowired private ObjectMapper objectMapper;

  private User user;
  private EmailVerificationToken verificationToken;

  @BeforeEach
  void setup() {
    user = User.builder().id(UUID.randomUUID()).username("username").build();
    objectMapper.registerModule(new JavaTimeModule());

    when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(new UsernamePasswordAuthenticationToken("username", "password"));

    when(jwtService.generateToken(user)).thenReturn("mockJwtToken");

    user = new User();
    user.setId(UUID.randomUUID());
    user.setEnabled(false);
    verificationToken = new EmailVerificationToken();
    verificationToken.setUser(user);
    verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
  }

  @Test
  void testLogin() throws Exception {
    LoginRequest req = new LoginRequest("username", "pass");
    String jsonRequest = objectMapper.writeValueAsString(req);

    mockMvc
        .perform(post(api + "/login").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("mockJwtToken"));
  }

  @Test
  void testRegister() throws Exception {
    RegisterRequest req = new RegisterRequest(
        "username", "firstName", "lastName", "password", "email@example.com", "1234567890");
    User user = User.builder()
                    .username(req.getUsername())
                    .firstName(req.getFirstName())
                    .lastName(req.getLastName())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .email(req.getEmail())
                    .phone(req.getPhone())
                    .role(Role.USER)
                    .enabled(false)
                    .firstLogin(true)
                    .hasConnectedBankAccount(false)
                    .hasCustomizedProfile(false)
                    .build();

    String jsonRequest = objectMapper.writeValueAsString(req);
    String token = "verificationToken";

    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(emailVerificationTokenService.generateVerificationToken(any(User.class)))
        .thenReturn(token);
    doNothing().when(mailService).sendVerificationEmail(any(User.class), anyString());

    mockMvc
        .perform(
            post(api + "/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isOk());

    verify(userRepository).save(any(User.class));
    verify(emailVerificationTokenService).generateVerificationToken(any(User.class));
    verify(mailService).sendVerificationEmail(any(User.class), eq(token));
  }

  @Test
  void testRegisterWithExistingUsername() throws Exception {
    RegisterRequest req = new RegisterRequest(
        "username", "firstName", "lastName", "password", "email@example.com", "1234567890");
    String jsonRequest = objectMapper.writeValueAsString(req);

    when(userRepository.save(any(User.class)))
        .thenThrow(new DataIntegrityViolationException("Username already exists"));

    mockMvc
        .perform(
            post(api + "/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isBadRequest());

    verify(userRepository).save(any(User.class));
    verifyNoInteractions(emailVerificationTokenService);
    verifyNoInteractions(mailService);
  }

  @Test
  void testVerifyEmailWithExpiredToken() throws Exception {
    verificationToken.setExpiryDate(LocalDateTime.now().minusDays(1)); // Token expired
    when(emailVerificationTokenService.getVerificationToken(any(String.class)))
        .thenReturn(verificationToken);

    mockMvc.perform(get(api + "/verify")).andExpect(status().isBadRequest());
  }

  @Test
  void testVerifyEmailWithNonExistentUser() throws Exception {
    when(emailVerificationTokenService.getVerificationToken("validToken"))
        .thenReturn(verificationToken);
    when(userRepository.findById(any(UUID.class)))
        .thenThrow(new IllegalStateException("User not found"));

    mockMvc.perform(get(api + "/verify")).andExpect(status().isBadRequest());
  }

  @Test
  void testVerifyEmailWithAlreadyVerifiedEmail() throws Exception {
    user.setEnabled(true);
    when(emailVerificationTokenService.getVerificationToken("validToken"))
        .thenReturn(verificationToken);
    when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

    mockMvc.perform(get(api + "/verify")).andExpect(status().isBadRequest());
  }

  // @Test
  // void testSuccessfulEmailVerification() throws Exception {
  //   when(emailVerificationTokenService.getVerificationToken(any(String.class)))
  //       .thenReturn(verificationToken);
  //   when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
  //   when(userRepository.save(any(User.class))).thenReturn(user);
  //   when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
  //
  //   mockMvc.perform(get(api + "/verify")).andExpect(status().isOk());
  // }
  //
  @Test
  void forgotPasswordSuccessful() throws Exception {
    EmailDTO emailDTO = new EmailDTO("user@example.com");
    String jsonRequest = objectMapper.writeValueAsString(emailDTO);
    User user = new User();
    user.setEmail("user@example.com");

    when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
    when(emailVerificationTokenService.generateVerificationToken(user)).thenReturn("resetToken");
    doNothing().when(mailService).sendResetPasswordEmail(user, "resetToken");

    mockMvc
        .perform(post(api + "/forgotPassword")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Check your email to reset password"));
  }

  @Test
  void resetPasswordWithExpiredToken() throws Exception {
    verificationToken.setExpiryDate(LocalDateTime.now().minusDays(1)); // Set token as expired
    ResetEmailDTO resetEmailDTO = new ResetEmailDTO("validToken", "newPassword");
    String jsonRequest = objectMapper.writeValueAsString(resetEmailDTO);

    when(emailVerificationTokenService.getVerificationToken(any(String.class)))
        .thenReturn(verificationToken);

    mockMvc
        .perform(post(api + "/resetPassword")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.token").value("Token expired"));
  }
}
