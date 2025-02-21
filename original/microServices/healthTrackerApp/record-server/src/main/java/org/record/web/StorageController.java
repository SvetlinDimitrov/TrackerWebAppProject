package org.record.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.custom.dto.CustomFoodRequestCreate;
import org.example.domain.food.shared.dto.FoodView;
import org.example.domain.storage.dto.StorageView;
import org.record.features.storage.services.StorageService;
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

@Tag(name = "Storage", description = "The Storage API provides operations for interacting with storages.")
@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {

  private final StorageService storageService;

  @Operation(summary = "Get all storages", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation",
          content = @Content(schema = @Schema(implementation = StorageView.class))),
      @ApiResponse(responseCode = "400", description = "Invalid User Token Header")})
  @GetMapping("/{recordId}")
  public ResponseEntity<List<StorageView>> getAll(
      @RequestHeader("X-ViewUser") String userToken, @PathVariable String recordId) {
    return new ResponseEntity<>(storageService.getAllByRecordId(recordId, userToken),
        HttpStatus.OK);
  }

  @Operation(summary = "Get storage by id", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation",
          content = @Content(schema = @Schema(implementation = StorageView.class))),
      @ApiResponse(responseCode = "400", description = "Storage Exception or Invalid User Token Header")})
  @GetMapping("/{id}")
  public ResponseEntity<StorageView> get(
      @RequestHeader("X-ViewUser") String userToken, @PathVariable String id) {
    return new ResponseEntity<>(
        storageService.getById(id, userToken),
        HttpStatus.OK);
  }

  @Operation(summary = "Create a new storage", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation",
          content = @Content(schema = @Schema(implementation = HttpStatus.class))),
      @ApiResponse(responseCode = "400", description = "Invalid User Token Header")})
  @PostMapping("/record/{recordId}")
  public ResponseEntity<HttpStatus> create(
      @RequestParam(required = false) String storageName,
      @RequestHeader("X-ViewUser") String userToken,
      @PathVariable String recordId) {
    storageService.create(recordId, storageName, userToken);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "Delete a storage", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Storage Exception or Invalid User Token Header")})
  @DeleteMapping("/{storageId}")
  ResponseEntity<HttpStatus> delete(
      @PathVariable String storageId, @RequestHeader("X-ViewUser") String userToken) {
    storageService.delete(storageId, userToken);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "Add food from storage", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Storage Exception, Food Exception or Invalid User Token Header")})
  @PatchMapping("/{storageId}/addFood")
  ResponseEntity<HttpStatus> addFoodFromStorage(
      @PathVariable String storageId, @RequestParam String recordId,
      @RequestBody CustomFoodRequestCreate foodDto, @RequestHeader("X-ViewUser") String userToken) {
    storageService.addFood(storageId, recordId, foodDto, userToken);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "Change food from storage", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Storage Exception, Food Exception or Invalid User Token Header")})
  @PatchMapping("/{storageId}/changeFood/{foodId}")
  ResponseEntity<HttpStatus> changeFoodFromStorage(
      @PathVariable String storageId, @RequestParam String recordId,
      @RequestBody CustomFoodRequestCreate foodDto, @RequestHeader("X-ViewUser") String userToken,
      @PathVariable String foodId) {
    storageService.changeFood(storageId, recordId, foodId, foodDto, userToken);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "Remove food from storage", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Food Exception, Storage Exception or Invalid User Token Header")})
  @PatchMapping("/{storageId}/removeFood")
  ResponseEntity<HttpStatus> removeFoodFromStorage(
      @PathVariable String storageId, @RequestParam String recordId,
      @RequestParam String foodId, @RequestHeader("X-ViewUser") String userToken) {
    storageService.removeFood(storageId, recordId, foodId, userToken);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "Get food by storage", responses = {
      @ApiResponse(responseCode = "200", description = "Successful operation",
          content = @Content(schema = @Schema(implementation = FoodView.class))),
      @ApiResponse(responseCode = "400", description = "Invalid User Token Header, Storage Exception or Food Exception")})
  @GetMapping("/{storageId}/getFood")
  ResponseEntity<FoodView> getFoodByStorage(
      @PathVariable String storageId, @RequestParam String recordId,
      @RequestParam String foodId, @RequestHeader("X-ViewUser") String userToken) {
    return new ResponseEntity<>(
        storageService.getFoodByStorage(storageId, recordId, foodId, userToken),
        HttpStatus.OK);
  }

}
