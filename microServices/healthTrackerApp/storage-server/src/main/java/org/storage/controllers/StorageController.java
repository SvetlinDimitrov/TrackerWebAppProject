package org.storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.storage.exception.FoodException;
import org.storage.exception.InvalidJsonTokenException;
import org.storage.exception.StorageException;
import org.storage.model.dto.FoodInsertDto;
import org.storage.model.dto.StorageView;
import org.storage.model.entity.Food;

import java.util.List;

@RestController
@RequestMapping("/api/storage")
public interface StorageController {

    @GetMapping("/all")
    public ResponseEntity<List<StorageView>> getAllStorages(
            @RequestParam String recordId,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @GetMapping
    public ResponseEntity<StorageView> getStorage(
            @RequestParam String recordId,
            @RequestParam String storageId,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @PostMapping
    public ResponseEntity<HttpStatus> createStorage(
            @RequestParam(required = false) String storageName,
            @RequestParam String recordId,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @DeleteMapping("/delete/{storageId}/record")
    public ResponseEntity<HttpStatus> deleteStorage(
            @PathVariable String storageId,
            @RequestParam String recordId,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @DeleteMapping("/delete/all")
    public ResponseEntity<HttpStatus> deleteAllStoragesByRecordId(
            @RequestParam String recordId,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @PatchMapping("/{storageId}/addFood")
    public ResponseEntity<HttpStatus> addFoodFromStorage(
            @PathVariable String storageId,
            @RequestParam String recordId,
            @RequestBody FoodInsertDto foodDto,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException;

    @PatchMapping("/{storageId}/changeFood")
    public ResponseEntity<HttpStatus> changeFoodFromStorage(
            @PathVariable String storageId,
            @RequestParam String recordId,
            @RequestBody FoodInsertDto foodDto,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException;

    @PatchMapping("/{storageId}/removeFood")
    public ResponseEntity<HttpStatus> removeFoodFromStorage(
            @PathVariable String storageId,
            @RequestParam String recordId,
            @RequestParam(defaultValue = "false") Boolean isCustom,
            @RequestParam String foodId,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws FoodException, StorageException, InvalidJsonTokenException;

    @GetMapping("/{storageId}/getFood")
    public ResponseEntity<Food> getFoodByStorage(
            @PathVariable String storageId,
            @RequestParam String recordId,
            @RequestParam(defaultValue = "false") Boolean isCustom,
            @RequestParam String foodName,
            @RequestHeader(name = "X-ViewUser") String userToken) throws InvalidJsonTokenException, StorageException, FoodException;
}
