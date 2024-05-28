package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.nutriGuideBuddy.domain.dto.user.*;
import org.nutriGuideBuddy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userServiceImp;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<JwtResponse> createUser(@RequestBody UserCreate userDto) {
    return userServiceImp.createUser(userDto);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<JwtResponse> loginUser(@RequestBody UserLogin dto) {
    return userServiceImp.loginUser(dto);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserView> getUserView() {
    return userServiceImp.getById();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteUser() {
    return userServiceImp.deleteUserById();
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<JwtResponse> modifyUsername(@RequestBody UserDto userDto) {
    return userServiceImp.modifyUsername(userDto);
  }

  @PatchMapping("/reset-password")
  public Mono<Void> modifyPassword(@RequestBody ResetPasswordDto dto) {
    return userServiceImp.modifyPassword(dto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
