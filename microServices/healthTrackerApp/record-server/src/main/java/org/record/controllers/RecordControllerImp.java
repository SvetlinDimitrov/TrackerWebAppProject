package org.record.controllers;

import lombok.RequiredArgsConstructor;
import org.record.exceptions.InvalidJsonTokenException;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.RecordView;
import org.record.services.RecordKafkaService;
import org.record.services.RecordServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecordControllerImp implements RecordController {

    private final RecordServiceImp recordService;
    private final RecordKafkaService recordKafkaService;

    @Override
    public ResponseEntity<List<RecordView>> getAllRecords(String userToken)
            throws UserNotFoundException, InvalidJsonTokenException {
        List<RecordView> records = recordService.getAllViewsByUserId(userToken);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RecordView> getById(String userToken, String recordId)
            throws RecordNotFoundException, InvalidJsonTokenException {
        RecordView record = recordService.getViewByRecordIdAndUserId(recordId, userToken);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> createNewRecord(String userToken, String name)
            throws RecordCreationException, InvalidJsonTokenException {
        recordKafkaService.addNewRecordByUserId(userToken, name);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteRecord(String recordId, String userToken)
            throws RecordNotFoundException, InvalidJsonTokenException {
        recordKafkaService.deleteById(recordId, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
