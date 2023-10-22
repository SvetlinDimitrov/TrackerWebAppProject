package org.record;

import lombok.RequiredArgsConstructor;
import org.record.exeptions.RecordCreationException;
import org.record.exeptions.RecordNotFoundException;
import org.record.model.dtos.ErrorResponse;
import org.record.model.dtos.RecordView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

    private final RecordServiceImp recordService;

    @GetMapping("/all")
    public ResponseEntity<List<RecordView>> getAllRecords(@RequestHeader("X-ViewUser") String userToken) {

        List<RecordView> records = recordService.getAllViewsByUserId(userToken);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordView> getById(@PathVariable(value = "recordId") Long recordId) throws RecordNotFoundException {
        RecordView record = recordService.getViewByRecordId(recordId);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<HttpStatus> createNewRecord(@RequestHeader("X-ViewUser") String userToken) throws RecordCreationException {

        recordService.addNewRecordByUserId(userToken);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/{recordId}")
    public ResponseEntity<HttpStatus> deleteRecord(@PathVariable Long recordId,
                                                   @RequestHeader("X-ViewUser") String userToken) throws RecordNotFoundException {

        recordService.deleteById(recordId, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }



    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchRecordNotFoundException(RecordNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(List.of(e.getMessage()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordCreationException.class)
    public ResponseEntity<ErrorResponse> catchRecordCreationException(RecordCreationException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getErrorMessages());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
