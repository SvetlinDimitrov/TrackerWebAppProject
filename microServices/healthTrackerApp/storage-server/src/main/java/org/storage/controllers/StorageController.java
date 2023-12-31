package org.storage.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Storage", description = "The Storage API provides operations for interacting with storages.")
public interface StorageController {

    @Operation(summary = "Get all storages", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = StorageView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = InvalidJsonTokenException.class)))})
    @GetMapping("/all")
    ResponseEntity<List<StorageView>> getAllStorages(
            @Parameter(description = "Record id") @RequestParam String recordId,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @Operation(summary = "Get storage by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = StorageView.class))),
            @ApiResponse(responseCode = "400", description = "Storage Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = StorageException.class)))})
    @GetMapping
    ResponseEntity<StorageView> getStorage(
            @Parameter(description = "Record id") @RequestParam String recordId,
            @Parameter(description = "Storage id") @RequestParam String storageId,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @Operation(summary = "Create a new storage", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class))),
            @ApiResponse(responseCode = "400", description = "Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = InvalidJsonTokenException.class)))})
    @PostMapping
    ResponseEntity<HttpStatus> createStorage(
            @Parameter(description = "Storage name", required = false) @RequestParam(required = false) String storageName,
            @Parameter(description = "Record id") @RequestParam String recordId,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @Operation(summary = "Delete a storage", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Storage Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = StorageException.class)))})
    @DeleteMapping("/delete/{storageId}/record")
    ResponseEntity<HttpStatus> deleteStorage(
            @Parameter(description = "Storage id") @PathVariable String storageId,
            @Parameter(description = "Record id") @RequestParam String recordId,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @Operation(summary = "Delete all storages by record id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Storage Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = StorageException.class)))})
    @DeleteMapping("/delete/all")
    ResponseEntity<HttpStatus> deleteAllStoragesByRecordId(
            @Parameter(description = "Record id") @RequestParam String recordId,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @Operation(summary = "Add food from storage", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Storage Exception, Food Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = StorageException.class)))})
    @PatchMapping("/{storageId}/addFood")
    ResponseEntity<HttpStatus> addFoodFromStorage(
            @Parameter(description = "Storage id") @PathVariable String storageId,
            @Parameter(description = "Record id") @RequestParam String recordId,
            @RequestBody FoodInsertDto foodDto,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException;

    @Operation(summary = "Change food from storage", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Storage Exception, Food Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = StorageException.class)))})
    @PatchMapping("/{storageId}/changeFood")
    ResponseEntity<HttpStatus> changeFoodFromStorage(
            @Parameter(description = "Storage id") @PathVariable String storageId,
            @Parameter(description = "Record id") @RequestParam String recordId,
            @RequestBody FoodInsertDto foodDto,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException;

    @Operation(summary = "Remove food from storage", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Food Exception, Storage Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @PatchMapping("/{storageId}/removeFood")
    ResponseEntity<HttpStatus> removeFoodFromStorage(
            @Parameter(description = "Storage id") @PathVariable String storageId,
            @Parameter(description = "Record id") @RequestParam String recordId,
            @Parameter(description = "Is custom") @RequestParam(defaultValue = "false") Boolean isCustom,
            @Parameter(description = "Food id") @RequestParam String foodId,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken)
            throws FoodException, StorageException, InvalidJsonTokenException;

    @Operation(summary = "Get food by storage", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Food.class))),
            @ApiResponse(responseCode = "400", description = "Invalid User Token Header, Storage Exception or Food Exception",
                    content = @Content(schema = @Schema(implementation = InvalidJsonTokenException.class)))})
    @GetMapping("/{storageId}/getFood")
    ResponseEntity<Food> getFoodByStorage(
            @Parameter(description = "Storage id") @PathVariable String storageId,
            @Parameter(description = "Record id") @RequestParam String recordId,
            @Parameter(description = "Is custom") @RequestParam(defaultValue = "false") Boolean isCustom,
            @Parameter(description = "Food id") @RequestParam String foodId,
            @Parameter(description = "User token") @RequestHeader("X-ViewUser") String userToken) throws InvalidJsonTokenException, StorageException, FoodException;
}
