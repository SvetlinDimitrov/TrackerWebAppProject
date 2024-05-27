package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.nutriGuideBuddy.domain.dto.user.UserEmailValidationCreate;
import org.nutriGuideBuddy.service.EmailVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/verify")
@RequiredArgsConstructor
public class VerificationController {

  private final EmailVerificationService emailVerificationService;


  @PostMapping("/send-email")
  public Mono<Void> sendVerificationEmail(@RequestBody UserEmailValidationCreate userDto) {
    return emailVerificationService.validateUserAndSendVerificationEmail(userDto);
  }

  @PostMapping("/forgot-password")
  public Mono<Void> sendForgotPasswordEmail(@RequestBody Map<String, String> request) {
    return emailVerificationService.sendForgotPasswordEmail(request.get("email"));
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
