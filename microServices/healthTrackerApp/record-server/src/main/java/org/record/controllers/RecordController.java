package org.record.controllers;

import java.util.List;

import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.RecordView;
import org.record.services.RecordServiceImp;
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
            throws RecordCreationException,
            RecordNotFoundException {

        recordService.addNewRecordByUserId(userToken, name);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<HttpStatus> deleteRecord(
            @PathVariable Long recordId,
            @RequestHeader("X-ViewUser") String userToken)
            throws RecordNotFoundException {

        recordService.deleteById(recordId, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
