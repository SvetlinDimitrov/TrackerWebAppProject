package org.record.controllers;

import java.util.List;

import org.record.exceptions.InvalidJsonTokenException;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.StorageException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.ErrorResponse;
import org.record.model.dtos.ErrorSingleResponse;
import org.record.model.dtos.RecordView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/record")
@Tag(name = "Record", description = "The Record API")
public interface RecordController {

    @Operation(summary = "Get all records", responses = {
            @ApiResponse(responseCode = "200", description = "List of all records", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecordView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))

    })
    @GetMapping("/all")
    public ResponseEntity<List<RecordView>> getAllRecords(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken)
            throws UserNotFoundException, InvalidJsonTokenException;

    @Operation(summary = "Get a record by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Record details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecordView.class))),
            @ApiResponse(responseCode = "400", description = "Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))

    })
    @GetMapping("/{recordId}")
    public ResponseEntity<RecordView> getById(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken,
            @Parameter(description = "ID of the record to be obtained.", example = "1") @PathVariable(value = "recordId") Long recordId)
            throws RecordNotFoundException, InvalidJsonTokenException;

    @Operation(summary = "Create a new record", responses = {
            @ApiResponse(responseCode = "201", description = "Record created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))

    })
    @PostMapping
    public ResponseEntity<HttpStatus> createNewRecord(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken,
            @Parameter(description = "Name of the new record.", example = "My Record") @RequestParam(required = false) String name)
            throws RecordCreationException, InvalidJsonTokenException;

    @Operation(summary = "Delete a record", responses = {
            @ApiResponse(responseCode = "204", description = "Record deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))

    })
    @DeleteMapping("/{recordId}")
    public ResponseEntity<HttpStatus> deleteRecord(
            @Parameter(description = "ID of the record to be deleted.", example = "1") @PathVariable Long recordId,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken)
            throws RecordNotFoundException, StorageException, InvalidJsonTokenException;

    @Operation(summary = "Create a storage", responses = {
            @ApiResponse(responseCode = "204", description = "Storage created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))

    })
    @PostMapping("/{recordId}/storage")
    public ResponseEntity<HttpStatus> createStorage(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken,
            @Parameter(description = "ID of the record where the storage will be created.", example = "1") @PathVariable Long recordId,
            @Parameter(description = "Name of the new storage.", example = "My Storage") @RequestParam(required = false) String storageName)
            throws RecordNotFoundException, InvalidJsonTokenException;

    @Operation(summary = "Remove a storage", responses = {
            @ApiResponse(responseCode = "204", description = "Storage removed", content = @Content),
            @ApiResponse(responseCode = "400", description = "Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))

    })
    @DeleteMapping("/{recordId}/storage/{storageId}")
    public ResponseEntity<HttpStatus> removeStorage(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken,
            @Parameter(description = "ID of the record where the storage will be removed.", example = "1") @PathVariable Long recordId,
            @Parameter(description = "ID of the storage to be removed.", example = "1") @PathVariable Long storageId)
            throws RecordNotFoundException, StorageException, InvalidJsonTokenException;
}