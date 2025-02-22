package org.food.infrastructure.exception;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.example.exceptions.throwable.BadRequestException;
import org.example.exceptions.throwable.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ProblemDetail> handleNotFoundException(NotFoundException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        e.getMessage());

    problemDetail.setTitle(e.getTitle());

    return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        e.getMessage());

    problemDetail.setTitle(e.getTitle());

    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    var list = ex.getBindingResult()
        .getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setTitle("Validation Error");
    problemDetail.setDetail("Validation failed for one or more fields.");
    problemDetail.setProperty("errors", list);

    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setTitle("Invalid Input");
    problemDetail.setDetail("Could not read the request body: " + ex.getLocalizedMessage());

    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraintViolationException(
      ConstraintViolationException ex) {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setTitle("Validation Error");

    String errorMessages = ex.getConstraintViolations()
        .stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.joining(", "));

    problemDetail.setDetail(errorMessages);

    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ProblemDetail> handleFeignStatusException(FeignException e) {
    HttpStatus status = HttpStatus.resolve(e.status());
    String rawContent = e.contentUTF8();

    String message = "An error occurred while processing your request.";

    if (rawContent != null) {
      try {
        JsonObject json = JsonParser.parseString(rawContent).getAsJsonObject();
        if (json.has("message")) {
          message = json.get("message").getAsString();
        }
      } catch (Exception parseException) {
        message = "Failed to parse error response.";
      }
    }

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR, message);
    problemDetail.setTitle("Feign Client Error");

    return new ResponseEntity<>(problemDetail,
        status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
