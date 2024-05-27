package org.nutriGuideBuddy.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.user.UserCreate;
import org.nutriGuideBuddy.domain.dto.user.UserEmailValidationCreate;
import org.nutriGuideBuddy.domain.entity.UserEntity;
import org.nutriGuideBuddy.repository.UserRepository;
import org.nutriGuideBuddy.utils.JWTUtilEmailValidation;
import org.nutriGuideBuddy.utils.user.UserModifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

  @Value("${sendinblue.api.key}")
  private String sendinblueApiKey;
  @Value("${frontend.url}")
  private String frontendUrl;
  private final UserRepository userRepository;
  private final JWTUtilEmailValidation JWTUtil;

  public Mono<Void> validateUserAndSendVerificationEmail(UserEmailValidationCreate dto) {
    return UserModifier.validateAndModifyUserCreation(new UserEntity(), dto)
        .map(UserEntity::getEmail)
        .flatMap(userRepository::findUserByEmail)
        .hasElement()
        .flatMap(userExists -> {
          if (userExists) {
            return Mono.error(new BadRequestException("User already exists"));
          } else {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(sendinblueApiKey);

            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
            String verificationUrl = frontendUrl + "/email-verification?token=" + JWTUtil.generateToken(dto.email());
            SendSmtpEmail email = getSendSmtpEmail(dto.email(), verificationUrl, "Email Verification", "Please verify your email by clicking the link below: ");
            try {
              apiInstance.sendTransacEmail(email);
              return Mono.empty();
            } catch (ApiException e) {
              return Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS));
            }
          }
        });
  }

  public Mono<Void> sendForgotPasswordEmail(String email) {
    return userRepository.findUserByEmail(email)
        .switchIfEmpty(Mono.error(new BadRequestException("User not found")))
        .flatMap(user -> {
          ApiClient defaultClient = Configuration.getDefaultApiClient();
          ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
          apiKey.setApiKey(sendinblueApiKey);

          TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
          String verificationUrl = frontendUrl + "/recreate-password?token=" + JWTUtil.generateToken(user.getEmail());
          SendSmtpEmail emailSend = getSendSmtpEmail(user.getEmail(), verificationUrl, "Reset Password", "Please reset your password by clicking the link below: ");

          try {
            apiInstance.sendTransacEmail(emailSend);
            return Mono.empty();
          } catch (ApiException e) {
            return Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS));
          }
        });
  }

  private static @NotNull SendSmtpEmail getSendSmtpEmail(String recipientEmail, String verificationUrl, String subject, String text) {
    SendSmtpEmailSender sender = new SendSmtpEmailSender();
    sender.setEmail("prolama6a@gmail.com");
    sender.setName("Dont replay");

    SendSmtpEmailTo to = new SendSmtpEmailTo();
    to.setEmail(recipientEmail);

    SendSmtpEmail email = new SendSmtpEmail();
    email.setSender(sender);
    email.setTo(List.of(to));
    email.setSubject(subject);
    email.setHtmlContent("<html><body><p>" + text + "<a href=\"" + verificationUrl + "\">Verify Email</a></p></body></html>");
    return email;
  }
}
