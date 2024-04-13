package org.trackerwebapp.user_server;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
public class UserController {

  private final UserService userServiceImp;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public Mono<JwtResponse> login(@RequestBody Map<String, String> requestBody) {
    return userServiceImp.login(requestBody.get("email"));
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
