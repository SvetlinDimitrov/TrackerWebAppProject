package org.record;

import java.util.List;

import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.StorageException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.ErrorResponse;
import org.record.model.dtos.ErrorSingleResponse;
import org.record.model.dtos.RecordView;
import org.record.services.RecordServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/record", headers = "X-ViewUser")
public class RecordController {

    private final RecordServiceImp recordService;

    @GetMapping("/all")
    public ResponseEntity<List<RecordView>> getAllRecords(
            @RequestHeader("X-ViewUser") String userToken)
            throws UserNotFoundException {

        List<RecordView> records = recordService.getAllViewsByUserId(userToken);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordView> getById(
            @RequestHeader("X-ViewUser") String userToken,
            @PathVariable(value = "recordId") Long recordId)
            throws RecordNotFoundException {
        RecordView record = recordService.getViewByRecordIdAndUserId(recordId, userToken);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createNewRecord(
            @RequestHeader("X-ViewUser") String userToken,
            @RequestParam(required = false) String name)
            throws RecordCreationException {

        recordService.addNewRecordByUserId(userToken, name);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<HttpStatus> deleteRecord(
            @PathVariable Long recordId,
            @RequestHeader("X-ViewUser") String userToken)
            throws RecordNotFoundException, StorageException {

        recordService.deleteById(recordId, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping("/{recordId}/storage")
    public ResponseEntity<HttpStatus> createStorage(
            @RequestHeader("X-ViewUser") String userToken,
            @PathVariable Long recordId,
            @RequestParam(required = false) String storageName) throws RecordNotFoundException {

        recordService.createNewStorage(recordId, storageName, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{recordId}/storage/{storageId}")
    public ResponseEntity<HttpStatus> removeStorage(
            @RequestHeader("X-ViewUser") String userToken,
            @PathVariable Long recordId,
            @PathVariable Long storageId) throws RecordNotFoundException, StorageException {

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
}
