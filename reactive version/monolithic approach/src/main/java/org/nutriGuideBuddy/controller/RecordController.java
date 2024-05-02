package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.nutriGuideBuddy.domain.dto.record.CreateRecord;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.nutriGuideBuddy.config.security.UserPrincipal;
import org.nutriGuideBuddy.domain.dto.record.RecordView;
import org.nutriGuideBuddy.service.RecordService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

  private final RecordService service;

  @PostMapping
  public Mono<RecordView> viewRecord(@AuthenticationPrincipal UserPrincipal user, @RequestBody CreateRecord dto) {
    return service.viewRecord(dto, user.getId());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
