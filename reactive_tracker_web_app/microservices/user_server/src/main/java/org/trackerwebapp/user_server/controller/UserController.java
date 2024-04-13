package org.trackerwebapp.user_server.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trackerwebapp.user_server.config.UserPrincipal;
import org.trackerwebapp.user_server.domain.dtos.ExceptionResponse;
import org.trackerwebapp.user_server.domain.dtos.JwtResponse;
import org.trackerwebapp.user_server.domain.dtos.UserDto;
import org.trackerwebapp.user_server.domain.dtos.UserView;
import org.trackerwebapp.user_server.exeption.UserException;
import org.trackerwebapp.user_server.service.UserService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userServiceImp;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JwtResponse> login(@RequestBody Map<String, String> requestBody) {
    return userServiceImp.login(requestBody.get("email"));
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> getUserView(@AuthenticationPrincipal UserPrincipal user) {
    return userServiceImp.getUserById(user.getId());
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> modifyUserDetails(@AuthenticationPrincipal UserPrincipal user,
      @RequestBody UserDto userDto) {
    return userServiceImp.modifyUser(user.getId(), userDto);
  }

  @ExceptionHandler(UserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(UserException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
