package org.record.controllers;

import org.record.exceptions.*;
import org.record.model.dtos.RecordView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
public interface RecordController {

    @GetMapping("/all")
    public ResponseEntity<List<RecordView>> getAllRecords(
            @RequestHeader("X-ViewUser") String userToken)
            throws UserNotFoundException, InvalidJsonTokenException;

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordView> getById(
            @RequestHeader("X-ViewUser") String userToken,
            @PathVariable(value = "recordId") String recordId)
            throws RecordNotFoundException, InvalidJsonTokenException;

    @PostMapping
    public ResponseEntity<HttpStatus> createNewRecord(
            @RequestHeader("X-ViewUser") String userToken,
            @RequestParam(required = false) String name)
            throws RecordCreationException, InvalidJsonTokenException;

    @DeleteMapping("/{recordId}")
    public ResponseEntity<HttpStatus> deleteRecord(
            @PathVariable String recordId,
            @RequestHeader("X-ViewUser") String userToken)
            throws RecordNotFoundException, StorageException, InvalidJsonTokenException;
}