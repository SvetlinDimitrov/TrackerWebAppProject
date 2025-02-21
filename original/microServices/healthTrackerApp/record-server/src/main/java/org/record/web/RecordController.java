package org.record.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.domain.record.dtos.RecordView;
import org.example.domain.record.paths.RecordPaths;
import org.record.features.record.services.RecordService;
import org.record.infrastructure.kafka.service.RecordKafkaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RecordPaths.BASE)
@RequiredArgsConstructor
@Tag(name = "Record", description = "The Record API provides operations for interacting with records.")
public class RecordController {

  private final RecordService recordService;

  @Operation(summary = "Get all records", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation",
          content = @Content(schema = @Schema(implementation = RecordView.class))),
      @ApiResponse(responseCode = "400", description = "User Not Found or Invalid Json Token")})
  @GetMapping(RecordPaths.GET_ALL)
  public ResponseEntity<List<RecordView>> getAll(String userToken) {
    List<RecordView> records = recordService.getAll(userToken);
    return new ResponseEntity<>(records, HttpStatus.OK);
  }

  @Operation(summary = "Get record by id", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation",
          content = @Content(schema = @Schema(implementation = RecordView.class))),
      @ApiResponse(responseCode = "400", description = "Record Not Found or Invalid Json Token")})
  @GetMapping(RecordPaths.GET_BY_ID)
  public ResponseEntity<RecordView> getById(String userToken, @PathVariable String id) {
    RecordView record = recordService.getById(id, userToken);
    return new ResponseEntity<>(record, HttpStatus.OK);
  }

  @Operation(summary = "Create a new record", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation",
          content = @Content(schema = @Schema(implementation = HttpStatus.class))),
      @ApiResponse(responseCode = "400", description = "Record Creation Exception or Invalid Json Token")})
  @PostMapping(RecordPaths.CREATE)
  public ResponseEntity<HttpStatus> create(String userToken, String name) {
    recordService.create(userToken, name);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(summary = "Delete a record", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Record Not Found, Storage Exception or Invalid Json Token")})
  @DeleteMapping(RecordPaths.DELETE)
  public ResponseEntity<HttpStatus> delete(String userToken, @PathVariable String id) {
    recordService.delete(id, userToken);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}