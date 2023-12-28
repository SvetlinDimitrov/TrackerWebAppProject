package org.storage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.storage.exception.FoodException;
import org.storage.exception.InvalidJsonTokenException;
import org.storage.exception.StorageException;
import org.storage.model.dto.FoodInsertDto;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Food;
import org.storage.services.StorageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StorageControllerImp implements StorageController {

    private final StorageService storageService;

    @Override
    public ResponseEntity<List<StorageView>> getAllStorages(
            String recordId,
            String userToken)
            throws InvalidJsonTokenException {
        return new ResponseEntity<>(storageService.getAllByRecordId(recordId, userToken), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StorageView> getStorage(
            String recordId,
            String storageId,
            String userToken)
            throws StorageException, InvalidJsonTokenException {
        return new ResponseEntity<>(storageService.getStorageByIdAndRecordId(storageId, recordId, userToken),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> createStorage(
            String storageName,
            String recordId,
            String userToken)
            throws InvalidJsonTokenException {
        storageService.createStorage(recordId, storageName, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteStorage(
            String storageId,
            String recordId,
            String userToken)
            throws StorageException, InvalidJsonTokenException {
        storageService.deleteStorage(recordId, storageId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteAllStoragesByRecordId(
            String recordId,
            String userToken)
            throws StorageException, InvalidJsonTokenException {
        storageService.deleteAllByRecordIdAndUserId(recordId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> addFoodFromStorage(
            String storageId,
            String recordId,
            FoodInsertDto foodDto,
            String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException {
        storageService.addFood(storageId, recordId, foodDto, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> changeFoodFromStorage(
            String storageId,
            String recordId,
            FoodInsertDto foodDto,
            String userToken) throws StorageException, FoodException, InvalidJsonTokenException {
        storageService.changeFood(storageId, recordId, foodDto, userToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HttpStatus> removeFoodFromStorage(
            String storageId,
            String recordId,
            Boolean isCustom,
            String foodId,
            String userToken) throws FoodException, StorageException, InvalidJsonTokenException {
        storageService.removeFood(storageId, recordId, foodId, userToken, isCustom);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Food> getFoodByStorage(
            String storageId,
            String recordId,
            Boolean isCustom,
            String foodName,
            String userToken) throws InvalidJsonTokenException, StorageException, FoodException {
        return new ResponseEntity<>(storageService.getFoodByStorage(storageId, recordId, foodName, userToken, isCustom),
                HttpStatus.OK);
    }

}
