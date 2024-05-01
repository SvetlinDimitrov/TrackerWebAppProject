package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.config.security.UserPrincipal;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.nutriGuideBuddy.domain.dto.user.UserCreate;
import org.nutriGuideBuddy.domain.dto.user.UserDto;
import org.nutriGuideBuddy.domain.dto.user.UserView;
import org.nutriGuideBuddy.service.UserService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userServiceImp;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<UserView> createUser(@RequestBody UserCreate userDto) {
    return userServiceImp.createUser(userDto);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> getUserView(@AuthenticationPrincipal UserPrincipal user) {
    return userServiceImp.getById(user.getId());
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteUser(@AuthenticationPrincipal UserPrincipal user) {
    return userServiceImp.deleteUserById(user.getId());
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> modifyUsername(@AuthenticationPrincipal UserPrincipal user, @RequestBody UserDto userDto) {
    return userServiceImp.modifyUsername(user.getId(), userDto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
