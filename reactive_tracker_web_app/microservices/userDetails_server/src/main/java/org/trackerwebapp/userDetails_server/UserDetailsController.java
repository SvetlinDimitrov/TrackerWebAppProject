package org.trackerwebapp.userDetails_server;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trackerwebapp.shared_interfaces.domain.dto.ExceptionResponse;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import org.trackerwebapp.userDetails_server.domain.dtos.UserDetailsDto;
import org.trackerwebapp.userDetails_server.domain.dtos.UserDetailsView;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/userDetails")
@RequiredArgsConstructor
public class UserDetailsController {

  private final UserDetailsService service;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserDetailsView> createUserDetails(
      @AuthenticationPrincipal User user,
      @RequestBody UserDetailsDto userDto) {
    return service.createUserDetails(user.getUsername(), userDto);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserDetailsView> getUserDetails(
      @AuthenticationPrincipal User user) {
    return service.getByUserId(user.getUsername());
  }

  @PatchMapping("/{detailsId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserDetailsView> modifyUserDetails(
      @AuthenticationPrincipal User user,
      @RequestBody UserDetailsDto userDto,
      @PathVariable String detailsId) {
    return service.modifyUserDetails(detailsId, user.getUsername(), userDto);
  }

  @DeleteMapping("/{detailsId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> deleteUserDetails(
      @AuthenticationPrincipal User user,
      @PathVariable String detailsId) {
    return service.deleteById(detailsId, user.getUsername());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
