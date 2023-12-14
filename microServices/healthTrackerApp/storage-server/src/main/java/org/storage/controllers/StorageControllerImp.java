package org.storage.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.storage.StorageService;
import org.storage.client.FoodUpdate;
import org.storage.exception.FoodException;
import org.storage.exception.InvalidJsonTokenException;
import org.storage.exception.StorageException;
import org.storage.model.dto.StorageView;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StorageControllerImp implements StorageController {

    private final StorageService storageService;

    @Override
    public ResponseEntity<List<StorageView>> getAllStorages(Long recordId, String userToken)
            throws InvalidJsonTokenException {
        return new ResponseEntity<>(storageService.getAllByRecordId(recordId, userToken), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StorageView> getStorage(Long recordId, Long storageId, String userToken)
            throws StorageException, InvalidJsonTokenException {
        return new ResponseEntity<>(storageService.getStorageByIdAndRecordId(storageId, recordId, userToken),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> createStorage(String storageName, Long recordId, String userToken)
            throws InvalidJsonTokenException {
        storageService.createStorage(recordId, storageName, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> createStorageFirstCreation(Long recordId, String userToken)
            throws InvalidJsonTokenException {
        storageService.firstCreationStorage(recordId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteStorage(Long storageId, Long recordId, String userToken)
            throws StorageException, InvalidJsonTokenException {
        storageService.deleteStorage(recordId, storageId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteAllStoragesByRecordId(Long recordId, String userToken)
            throws StorageException, InvalidJsonTokenException {
        storageService.deleteAllByRecordIdAndUserId(recordId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> addFoodFromStorage(
            Long storageId, Long recordId, Boolean isCustom,
            FoodUpdate foodInfo, String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException {
        storageService.addFood(storageId, recordId, foodInfo, userToken, isCustom);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> changeFoodFromStorage(Long storageId, Long recordId, Boolean isCustom,
            FoodUpdate foodInfo, String userToken) throws StorageException, FoodException, InvalidJsonTokenException {
        storageService.changeFood(storageId, recordId, foodInfo, userToken, isCustom);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> removeFoodFromStorage(Long storageId, Long recordId, Boolean isCustom,
            String foodName, String userToken) throws FoodException, StorageException, InvalidJsonTokenException {
        storageService.removeFood(storageId, recordId, foodName, userToken, isCustom);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
