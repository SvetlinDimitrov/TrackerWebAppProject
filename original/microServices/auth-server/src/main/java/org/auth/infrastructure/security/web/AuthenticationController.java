package org.auth.infrastructure.security.web;

import static org.auth.infrastructure.exceptions.ExceptionMessages.INVALID_USERNAME_OR_PASSWORD;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.auth.features.user.services.UserService;
import org.auth.infrastructure.security.dto.AuthenticationRequest;
import org.auth.infrastructure.security.dto.AuthenticationResponse;
import org.auth.infrastructure.security.services.JwtService;
import org.example.exceptions.throwable.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
      @Valid @RequestBody AuthenticationRequest authenticationRequest
  ) {
    var user = userService.findByEmail(authenticationRequest.email());

    if (passwordEncoder.matches(authenticationRequest.password(), user.getPassword())) {
      return ResponseEntity
          .ok()
          .body(jwtService.generateToken(user));
    }
    throw new BadRequestException(INVALID_USERNAME_OR_PASSWORD);
  }
}