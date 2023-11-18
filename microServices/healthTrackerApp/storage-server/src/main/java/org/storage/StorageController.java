package org.storage;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.storage.exception.FoodNameNotFoundException;
import org.storage.exception.FoodNotFoundException;
import org.storage.exception.StorageNotFoundException;
import org.storage.model.dto.StorageUpdate;
import org.storage.model.dto.StorageView;
import org.storage.model.errorResponses.ErrorResponse;
import org.storage.model.errorResponses.FoodErrorResponse;
import org.storage.services.StorageKafkaService;
import org.storage.services.StorageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/api/storage", headers = "X-ViewUser")
public class StorageController {

    private final StorageService storageService;
    private final StorageKafkaService storageKafkaService;

    @GetMapping
    public ResponseEntity<List<StorageView>> getAllStorages(@RequestParam (required = true) Long recordId) {
        List<StorageView> allByRecordId = storageService.getAllByRecordId(recordId);
        return new ResponseEntity<>(allByRecordId, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<StorageView>> firstCreationOfRecord(@RequestParam (required = true) Long recordId) {
        List<StorageView> allByRecordId = storageService.recordFirstCreation(recordId);
        return new ResponseEntity<>(allByRecordId, HttpStatus.CREATED);
    }

    @PatchMapping("/addFood")
    public ResponseEntity<HttpStatus> addFoodToStorage(@RequestBody StorageUpdate dto)
            throws StorageNotFoundException, FoodNotFoundException {
        storageKafkaService.addFood(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/removeFood")
    public ResponseEntity<HttpStatus> removeFoodFromStorage(@RequestBody StorageUpdate dto)
            throws StorageNotFoundException, FoodNameNotFoundException {
        storageKafkaService.removeFoodFromStorage(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(StorageNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchStorageNotFoundError(StorageNotFoundException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FoodNotFoundException.class)
    public ResponseEntity<FoodErrorResponse> catchFoodNotFoundError(FoodNotFoundException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FoodNameNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchFoodNameNotFoundException(FoodNameNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }
}
