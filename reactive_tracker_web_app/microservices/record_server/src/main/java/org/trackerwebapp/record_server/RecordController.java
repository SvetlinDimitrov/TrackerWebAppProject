package org.trackerwebapp.record_server;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trackerwebapp.record_server.domain.dto.CreateRecord;
import org.trackerwebapp.record_server.domain.dto.ModifyRecord;
import org.trackerwebapp.record_server.domain.dto.RecordView;
import org.trackerwebapp.shared_interfaces.domain.dto.ExceptionResponse;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

  private final RecordService service;

  @GetMapping
  public Flux<RecordView> getAllRecordsByUserId(
      @AuthenticationPrincipal User user,
      @RequestParam(name = "moreThenDailyCalorie", required = false) BigDecimal moreThenDailyCalorie) {
    return service.getRecordsByUserId(user.getUsername() , moreThenDailyCalorie);
  }

  @GetMapping("/{recordId}")
  public Mono<RecordView> getRecordById(
      @PathVariable String recordId,
      @AuthenticationPrincipal User user) {
    return service.getRecordByIdAndUserId(recordId, user.getUsername());
  }

  @PostMapping
  public Mono<RecordView> createRecord(
      @AuthenticationPrincipal User user,
      @RequestBody CreateRecord dto) {
    return service.createRecord(dto, user.getUsername());
  }

  @PatchMapping("/{recordId}")
  public Mono<RecordView> modifyRecord(
      @PathVariable String recordId,
      @AuthenticationPrincipal User user,
      @RequestBody ModifyRecord dto) {
    return service.modifyRecord(recordId, user.getUsername(), dto);
  }

  @DeleteMapping("/{recordId}")
  public Mono<Void> deleteRecord(
      @PathVariable String recordId,
      @AuthenticationPrincipal User user) {
    return service.deleteByIdAndUserId(recordId, user.getUsername());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
