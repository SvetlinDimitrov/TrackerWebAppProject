package org.storage;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.storage.exception.FoodNotFoundException;
import org.storage.exception.StorageNotFoundException;
import org.storage.model.dto.RemoveFoodStorage;
import org.storage.model.dto.StorageFill;
import org.storage.model.dto.StorageView;
import org.storage.model.errorResponses.ErrorResponse;
import org.storage.model.errorResponses.FoodErrorResponse;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<StorageView>> getAllStorages(@RequestParam Long recordId) {
        List<StorageView> allByRecordId = storageService.getAllByRecordId(recordId);
        return new ResponseEntity<>(allByRecordId, HttpStatus.OK);
    }

    @PatchMapping("/addFood")
    public ResponseEntity<HttpStatus> addFoodToStorage(@RequestBody StorageFill dto)
            throws StorageNotFoundException, FoodNotFoundException {
        storageService.addFood(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/removeFood")
    public ResponseEntity<HttpStatus> removeFoodFromStorage(@RequestBody RemoveFoodStorage dto)
            throws StorageNotFoundException, FoodNotFoundException {
        storageService.removeFoodFromStorage(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(StorageNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchMacroNotFoundError(StorageNotFoundException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FoodNotFoundException.class)
    public ResponseEntity<FoodErrorResponse> catchMacroNotFoundError(FoodNotFoundException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
}
