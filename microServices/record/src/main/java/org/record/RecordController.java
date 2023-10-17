package org.record;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.record.exeptions.RecordCreationException;
import org.record.exeptions.RecordNotFoundException;
import org.record.model.dtos.ErrorResponse;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.RecordView;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

    private final RecordServiceImp recordService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<RecordView>> getAllRecords(@PathVariable(value = "userId") Long userId) {
        List<RecordView> records = recordService.getAllViewsByUserId(userId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordView> getById(@PathVariable(value = "recordId") Long recordId) throws RecordNotFoundException {
        RecordView record = recordService.getViewByRecordId(recordId);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }


    @PostMapping("/{userId}")
    public ResponseEntity<HttpStatus> createNewRecord(@Valid @RequestBody RecordCreateDto recordCreateDto,
                                                      BindingResult result,
                                                      @PathVariable(value = "userId") Long userId) throws RecordCreationException {

        if(result.hasErrors()){
            throw new RecordCreationException(result.getFieldErrors());
        }

        recordService.addNewRecordByUserId(userId , recordCreateDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/{userId}/{recordId}")
    public ResponseEntity<HttpStatus> deleteRecord(@PathVariable Long recordId,
                                                   @PathVariable Long userId) throws RecordNotFoundException {

        recordService.deleteById(recordId, userId);

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
