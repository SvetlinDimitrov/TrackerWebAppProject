package org.record.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.record.exceptions.*;
import org.record.model.dtos.RecordView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@Tag(name = "Record", description = "The Record API provides operations for interacting with records.")
public interface RecordController {

    @Operation(summary = "Get all records", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = RecordView.class))),
            @ApiResponse(responseCode = "400", description = "User Not Found or Invalid Json Token",
                    content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))})
    @GetMapping("/all")
    public ResponseEntity<List<RecordView>> getAllRecords(
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws UserNotFoundException, InvalidJsonTokenException;

    @Operation(summary = "Get record by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = RecordView.class))),
            @ApiResponse(responseCode = "400", description = "Record Not Found or Invalid Json Token",
                    content = @Content(schema = @Schema(implementation = RecordNotFoundException.class)))})
    @GetMapping("/{recordId}")
    public ResponseEntity<RecordView> getById(
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken,
            @Parameter(description = "Record id") @PathVariable(value = "recordId") String recordId)
            throws RecordNotFoundException, InvalidJsonTokenException;

    @Operation(summary = "Create a new record", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class))),
            @ApiResponse(responseCode = "400", description = "Record Creation Exception or Invalid Json Token",
                    content = @Content(schema = @Schema(implementation = RecordCreationException.class)))})
    @PostMapping
    public ResponseEntity<HttpStatus> createNewRecord(
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken,
            @Parameter(description = "Name", required = false) @RequestParam(required = false) String name)
            throws RecordCreationException, InvalidJsonTokenException;

    @Operation(summary = "Delete a record", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Record Not Found, Storage Exception or Invalid Json Token",
                    content = @Content(schema = @Schema(implementation = RecordNotFoundException.class)))})
    @DeleteMapping("/{recordId}")
    public ResponseEntity<HttpStatus> deleteRecord(
            @Parameter(description = "Record id") @PathVariable String recordId,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws RecordNotFoundException, StorageException, InvalidJsonTokenException;
}