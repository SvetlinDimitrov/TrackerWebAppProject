package org.trackerwebapp.trackerwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.trackerwebapp.trackerwebapp.config.security.UserPrincipal;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.ExceptionResponse;
import org.trackerwebapp.trackerwebapp.domain.dto.record.CreateRecord;
import org.trackerwebapp.trackerwebapp.domain.dto.record.RecordView;
import org.trackerwebapp.trackerwebapp.service.RecordService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

  private final RecordService service;

  //TODO: CREATE A WAY TO SET CUSTOM DALY NUTRITION VALUES IF THE PROVIDED ONES ARE NOT APPROPRIATED FROM THE VIEWER
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
