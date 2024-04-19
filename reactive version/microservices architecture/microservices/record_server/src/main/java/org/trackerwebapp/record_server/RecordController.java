package org.trackerwebapp.record_server;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trackerwebapp.record_server.RecordService;
import org.trackerwebapp.record_server.domain.dto.CreateRecord;
import org.trackerwebapp.record_server.domain.dto.RecordView;
import org.trackerwebapp.shared_interfaces.domain.dto.ExceptionResponse;
import org.trackerwebapp.shared_interfaces.domain.enums.Goals;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

  private final RecordService service;

  @PostMapping
  public Mono<RecordView> viewRecord(
      @AuthenticationPrincipal User user,
      @RequestBody CreateRecord dto) {
    return service.viewRecord(dto, user.getUsername());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
