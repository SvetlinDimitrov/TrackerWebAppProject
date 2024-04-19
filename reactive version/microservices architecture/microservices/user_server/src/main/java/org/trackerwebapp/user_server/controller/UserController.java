package org.trackerwebapp.user_server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;
import org.trackerwebapp.shared_interfaces.domain.dto.ExceptionResponse;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import org.trackerwebapp.user_server.domain.dtos.JwtResponse;
import org.trackerwebapp.user_server.domain.dtos.UserDto;
import org.trackerwebapp.user_server.domain.dtos.UserView;
import org.trackerwebapp.user_server.service.UserService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userServiceImp;

  @GetMapping("/generateToken")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JwtResponse> login(Authentication authentication) {
    OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
    String email = principal.getAttribute("email");
    log.info("Email logged: {}", email);
    return userServiceImp.login(email);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> getUserView(@AuthenticationPrincipal User user) {
    return userServiceImp.getById(user.getUsername());
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> deleteUser(@AuthenticationPrincipal User user) {
    return userServiceImp.deleteUserById(user.getUsername());
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> modifyUsername(@AuthenticationPrincipal User user,
      @RequestBody UserDto userDto) {
    return userServiceImp.modifyUsername(user.getUsername(), userDto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
