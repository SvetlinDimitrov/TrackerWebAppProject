package org.storage;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.storage.client.FoodUpdate;
import org.storage.exception.FoodException;
import org.storage.exception.StorageException;
import org.storage.model.dto.StorageView;
import org.storage.model.errorResponses.ErrorResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/storage", headers = "X-ViewUser")
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/all")
    public ResponseEntity<List<StorageView>> getAllStorages(
            @RequestParam Long recordId,
            @RequestHeader(name = "X-ViewUser") String userToken) throws StorageException {
        return new ResponseEntity<>(storageService.getAllByRecordId(recordId, userToken), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<StorageView> getStorage(
            @RequestParam Long recordId,
            @RequestParam Long storageId,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException {
        return new ResponseEntity<>(storageService.getStorageByIdAndRecordId(storageId, recordId, userToken),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createStorage(
            @RequestParam(required = false) String storageName,
            @RequestParam Long recordId,
            @RequestHeader(name = "X-ViewUser") String userToken) {
        storageService.createStorage(recordId, storageName, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/firstCreation")
    public ResponseEntity<HttpStatus> createStorageFirstCreation(
            @RequestParam Long recordId,
            @RequestHeader(name = "X-ViewUser") String userToken) {
        storageService.firstCreationStorage(recordId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/{storageId}/record")
    public ResponseEntity<HttpStatus> deleteStorage(
            @PathVariable Long storageId,
            @RequestParam Long recordId,
            @RequestHeader(name = "X-ViewUser") String userToken) throws StorageException {
        storageService.deleteStorage(recordId, storageId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<HttpStatus> deleteAllStoragesByRecordId(
            @RequestParam Long recordId,
            @RequestHeader(name = "X-ViewUser") String userToken) throws StorageException {
        storageService.deleteAllByRecordIdAndUserId(recordId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{storageId}/addFood")
    public ResponseEntity<HttpStatus> addFoodFromStorage(
            @PathVariable Long storageId,
            @RequestParam Long recordId,
            @RequestBody FoodUpdate foodInfo,
            @RequestHeader(name = "X-ViewUser") String userToken) throws StorageException, FoodException {

        storageService.addFood(storageId, recordId, foodInfo, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{storageId}/changeFood")
    public ResponseEntity<HttpStatus> changeFoodFromStorage(
            @PathVariable Long storageId,
            @RequestParam Long recordId,
            @RequestBody FoodUpdate foodInfo,
            @RequestHeader(name = "X-ViewUser") String userToken) throws StorageException, FoodException {

        storageService.changeFood(storageId, recordId, foodInfo, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{storageId}/removeFood")
    public ResponseEntity<HttpStatus> removeFoodFromStorage(
            @PathVariable Long storageId,
            @RequestParam Long recordId,
            @RequestParam String foodName,
            @RequestHeader(name = "X-ViewUser") String userToken) throws FoodException, StorageException {

        storageService.removeFood(storageId, recordId, foodName, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> catchRecordNotFoundException(StorageException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FoodException.class)
    public ResponseEntity<ErrorResponse> catchRecordNotFoundException(FoodException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
