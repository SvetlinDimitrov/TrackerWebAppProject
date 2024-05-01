package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.config.security.UserPrincipal;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.nutriGuideBuddy.domain.dto.user.UserDetailsDto;
import org.nutriGuideBuddy.domain.dto.user.UserDetailsView;
import org.nutriGuideBuddy.service.UserDetailsService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user/details")
@RequiredArgsConstructor
public class UserDetailsController {

  private final UserDetailsService service;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserDetailsView> getUserDetails(@AuthenticationPrincipal UserPrincipal user) {
    return service.getByUserId(user.getId());
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<UserDetailsView> modifyUserDetails(@AuthenticationPrincipal UserPrincipal user, @RequestBody UserDetailsDto userDto) {
    return service.modifyUserDetails(user.getId(), userDto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
