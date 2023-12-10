package org.record;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.record.exceptions.*;
import org.record.model.dtos.ErrorResponse;
import org.record.model.dtos.ErrorSingleResponse;
import org.record.model.dtos.RecordView;
import org.record.services.RecordServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@Tag(name = "Record", description = "The Record API")
@RequiredArgsConstructor
public class RecordController {
    
    private final RecordServiceImp recordService;
    
    @Operation(summary = "Get all records", responses = {
        @ApiResponse(responseCode = "200", description = "List of all records", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecordView.class))),
        @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
        
    })
    @GetMapping("/all")
    public ResponseEntity<List<RecordView>> getAllRecords(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken)
        throws UserNotFoundException, InvalidJsonTokenException {
        
        List<RecordView> records = recordService.getAllViewsByUserId(userToken);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }
    
    @Operation(summary = "Get a record by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Record details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecordView.class))),
        @ApiResponse(responseCode = "400", description = "Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
        
    })
    @GetMapping("/{recordId}")
    public ResponseEntity<RecordView> getById(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken,
        @Parameter(description = "ID of the record to be obtained.", example = "1") @PathVariable(value = "recordId") Long recordId)
        throws RecordNotFoundException, InvalidJsonTokenException {
        RecordView record = recordService.getViewByRecordIdAndUserId(recordId, userToken);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    
    @Operation(summary = "Create a new record", responses = {
        @ApiResponse(responseCode = "201", description = "Record created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
        
    })
    @PostMapping
    public ResponseEntity<HttpStatus> createNewRecord(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken,
        @Parameter(description = "Name of the new record.", example = "My Record") @RequestParam(required = false) String name)
        throws RecordCreationException, InvalidJsonTokenException {
        
        recordService.addNewRecordByUserId(userToken, name);
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @Operation(summary = "Delete a record", responses = {
        @ApiResponse(responseCode = "204", description = "Record deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
        
    })
    @DeleteMapping("/{recordId}")
    public ResponseEntity<HttpStatus> deleteRecord(
        @Parameter(description = "ID of the record to be deleted.", example = "1") @PathVariable Long recordId,
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken)
        throws RecordNotFoundException, StorageException, InvalidJsonTokenException {
        
        recordService.deleteById(recordId, userToken);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        
    }
    
    @Operation(summary = "Create a storage", responses = {
        @ApiResponse(responseCode = "204", description = "Storage created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
        
    })
    @PostMapping("/{recordId}/storage")
    public ResponseEntity<HttpStatus> createStorage(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken,
        @Parameter(description = "ID of the record where the storage will be created.", example = "1") @PathVariable Long recordId,
        @Parameter(description = "Name of the new storage.", example = "My Storage") @RequestParam(required = false) String storageName) throws RecordNotFoundException, InvalidJsonTokenException {
        
        recordService.createNewStorage(recordId, storageName, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @Operation(summary = "Remove a storage", responses = {
        @ApiResponse(responseCode = "204", description = "Storage removed", content = @Content),
        @ApiResponse(responseCode = "400", description = "Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
        
    })
    @DeleteMapping("/{recordId}/storage/{storageId}")
    public ResponseEntity<HttpStatus> removeStorage(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader("X-ViewUser") String userToken,
        @Parameter(description = "ID of the record where the storage will be removed.", example = "1") @PathVariable Long recordId,
        @Parameter(description = "ID of the storage to be removed.", example = "1") @PathVariable Long storageId) throws RecordNotFoundException, StorageException, InvalidJsonTokenException {
        
        recordService.removeStorage(recordId, storageId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorSingleResponse> catchRecordNotFoundException(RecordNotFoundException e) {
        
        ErrorSingleResponse errorResponse = new ErrorSingleResponse();
        errorResponse.setErrorMessage(e.getMessage());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RecordCreationException.class)
    public ResponseEntity<ErrorResponse> catchRecordCreationException(RecordCreationException e) {
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getErrorMessages());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorSingleResponse> catchUserNotFoundException(UserNotFoundException e) {
        
        ErrorSingleResponse errorResponse = new ErrorSingleResponse();
        errorResponse.setErrorMessage(e.getMessage());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorSingleResponse> catchStorageException(StorageException e) {
        
        ErrorSingleResponse errorResponse = new ErrorSingleResponse();
        errorResponse.setErrorMessage(e.getMessage());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidJsonTokenException.class)
    public ResponseEntity<ErrorSingleResponse> catchInvalidJsonTokenException(InvalidJsonTokenException e) {
        
        ErrorSingleResponse errorResponse = new ErrorSingleResponse();
        errorResponse.setErrorMessage(e.getMessage());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
