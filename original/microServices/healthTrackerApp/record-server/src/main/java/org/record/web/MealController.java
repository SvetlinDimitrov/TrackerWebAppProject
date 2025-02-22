package org.record.web;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.record.features.meal.dto.MealRequest;
import org.record.features.meal.dto.MealView;
import org.record.features.meal.services.MealService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/meal")
@RequiredArgsConstructor
public class MealController {

  private final MealService service;

  @GetMapping
  public ResponseEntity<Page<MealView>> getAll(
      @RequestHeader("X-ViewUser") String userToken, @RequestParam UUID recordId,
      Pageable pageable) {
    return new ResponseEntity<>(
        service.getAll(recordId, userToken, pageable),
        HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MealView> get(
      @RequestHeader("X-ViewUser") String userToken, @PathVariable UUID id) {
    return new ResponseEntity<>(
        service.getById(id, userToken),
        HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<MealView> create(
      @RequestParam UUID recordId, @RequestBody @Valid MealRequest dto,
      @RequestHeader("X-ViewUser") String userToken) {
    return new ResponseEntity<>(
        service.create(recordId, dto, userToken),
        HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<MealView> update(
      @RequestHeader("X-ViewUser") String userToken, @PathVariable UUID id,
      @RequestBody @Valid MealRequest dto) {
    return new ResponseEntity<>(
        service.update(id, dto, userToken),
        HttpStatus.OK);
  }

  @DeleteMapping("/{storageId}")
  ResponseEntity<HttpStatus> delete(
      @PathVariable UUID storageId, @RequestHeader("X-ViewUser") String userToken) {
    service.delete(storageId, userToken);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
