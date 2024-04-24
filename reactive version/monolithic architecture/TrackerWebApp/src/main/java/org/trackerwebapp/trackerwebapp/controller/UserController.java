package org.trackerwebapp.trackerwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.trackerwebapp.trackerwebapp.config.security.UserPrincipal;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.ExceptionResponse;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserCreate;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserDto;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserView;
import org.trackerwebapp.trackerwebapp.service.UserService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userServiceImp;

  @PostMapping
  public Mono<UserView> createUser(@RequestBody UserCreate userDto) {
    return (Mono<UserView>) userServiceImp.createUser(userDto);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> getUserView(@AuthenticationPrincipal UserPrincipal user) {
    return userServiceImp.getById(user.getId());
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> deleteUser(@AuthenticationPrincipal User user) {
    return userServiceImp.deleteUserById(user.getUsername());
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> modifyUsername(@AuthenticationPrincipal User user, @RequestBody UserDto userDto) {
    return userServiceImp.modifyUsername(user.getUsername(), userDto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
