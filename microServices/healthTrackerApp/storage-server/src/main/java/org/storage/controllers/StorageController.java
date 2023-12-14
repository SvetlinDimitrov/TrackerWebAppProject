package org.storage.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.storage.exception.InvalidJsonTokenException;
import org.storage.exception.StorageException;
import org.storage.model.dto.StorageView;
import org.storage.model.errorResponses.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/storage")
@Tag(name = "Storage", description = "The Storage API. Warning: This server highly depends on the Food server.")
public interface StorageController {

    @Operation(summary = "Get all storages", responses = {
            @ApiResponse(responseCode = "200", description = "List of all storages", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StorageView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<StorageView>> getAllStorages(
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @Operation(summary = "Get a storage", responses = {
            @ApiResponse(responseCode = "200", description = "Storage details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StorageView.class))),
            @ApiResponse(responseCode = "400", description = "Storage not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })

    @GetMapping
    public ResponseEntity<StorageView> getStorage(
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "Storage ID.", example = "1") @RequestParam Long storageId,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @Operation(summary = "Create a storage", responses = {
            @ApiResponse(responseCode = "204", description = "Storage created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<HttpStatus> createStorage(
            @Parameter(description = "Storage name.", example = "My Storage") @RequestParam(required = false) String storageName,
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @PostMapping("/firstCreation")
    @Operation(summary = "Create a storage for the first time", responses = {
            @ApiResponse(responseCode = "204", description = "Storage created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HttpStatus> createStorageFirstCreation(
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @DeleteMapping("/delete/{storageId}/record")
    @Operation(summary = "Delete a storage", responses = {
            @ApiResponse(responseCode = "204", description = "Storage deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HttpStatus> deleteStorage(
            @Parameter(description = "Storage ID.", example = "1") @PathVariable Long storageId,
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @DeleteMapping("/delete/all")
    @Operation(summary = "Delete all storages by record ID", responses = {
            @ApiResponse(responseCode = "204", description = "All storages deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<HttpStatus> deleteAllStoragesByRecordId(
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, InvalidJsonTokenException;

    @PatchMapping("/{storageId}/addFood")
    @Operation(summary = "Add food to a storage", responses = {
            @ApiResponse(responseCode = "204", description = "Food added to storage", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodUpdate.class), examples = @ExampleObject(name = "foodUpdate", value = "{\"foodName\":\"Apple\",\"amount\":100}"))))
    public ResponseEntity<HttpStatus> addFoodFromStorage(
            @Parameter(description = "Storage ID.", example = "1") @PathVariable Long storageId,
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "Is custom food.", example = "false") @RequestParam(defaultValue = "false") Boolean isCustom,
            @Parameter(description = "Food information.", schema = @Schema(implementation = FoodUpdate.class)) @RequestBody FoodUpdate foodInfo,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException;

    @PatchMapping("/{storageId}/changeFood")
    @Operation(summary = "Change food in a storage", responses = {
            @ApiResponse(responseCode = "204", description = "Food changed in storage", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodUpdate.class), examples = @ExampleObject(name = "foodUpdate", value = "{\"foodName\":\"Apple\",\"amount\":100}"))))
    public ResponseEntity<HttpStatus> changeFoodFromStorage(
            @Parameter(description = "Storage ID.", example = "1") @PathVariable Long storageId,
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "Is custom food.", example = "false") @RequestParam(defaultValue = "false") Boolean isCustom,
            @Parameter(description = "Food information.", schema = @Schema(implementation = FoodUpdate.class)) @RequestBody FoodUpdate foodInfo,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws StorageException, FoodException, InvalidJsonTokenException;

    @PatchMapping("/{storageId}/removeFood")
    @Operation(summary = "Remove food from a storage", responses = {
            @ApiResponse(responseCode = "204", description = "Food removed from storage", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

    })
    public ResponseEntity<HttpStatus> removeFoodFromStorage(
            @Parameter(description = "Storage ID.", example = "1") @PathVariable Long storageId,
            @Parameter(description = "Record ID.", example = "1") @RequestParam Long recordId,
            @Parameter(description = "Is custom food.", example = "false") @RequestParam(defaultValue = "false") Boolean isCustom,
            @Parameter(description = "Food name.", example = "Apple") @RequestParam String foodName,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws FoodException, StorageException, InvalidJsonTokenException;
}
