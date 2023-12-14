package org.record.controllers;

import java.util.List;

import org.record.exceptions.InvalidJsonTokenException;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.StorageException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.RecordView;
import org.record.services.RecordServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RecordControllerImp implements RecordController {

    private final RecordServiceImp recordService;

    @Override
    public ResponseEntity<List<RecordView>> getAllRecords(String userToken)
            throws UserNotFoundException, InvalidJsonTokenException {
        List<RecordView> records = recordService.getAllViewsByUserId(userToken);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RecordView> getById(String userToken, Long recordId)
            throws RecordNotFoundException, InvalidJsonTokenException {
        RecordView record = recordService.getViewByRecordIdAndUserId(recordId, userToken);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> createNewRecord(String userToken, String name)
            throws RecordCreationException, InvalidJsonTokenException {
        recordService.addNewRecordByUserId(userToken, name);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteRecord(Long recordId, String userToken)
            throws RecordNotFoundException, StorageException, InvalidJsonTokenException {
        recordService.deleteById(recordId, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> createStorage(String userToken, Long recordId, String storageName)
            throws RecordNotFoundException, InvalidJsonTokenException {
        recordService.createNewStorage(recordId, storageName, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> removeStorage(String userToken, Long recordId, Long storageId)
            throws RecordNotFoundException, StorageException, InvalidJsonTokenException {
        recordService.removeStorage(recordId, storageId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
