package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.nutriGuideBuddy.domain.dto.user.JwtResponse;
import org.springframework.http.HttpStatus;
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
  public Mono<UserDetailsView> getUserDetails() throws BadRequestException {
    return service.getByUserId();
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<JwtResponse> modifyUserDetails(@RequestBody UserDetailsDto userDto) {
    return service.modifyUserDetails(userDto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
