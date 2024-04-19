package org.food.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.foodView.CustomFoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/food")
@Tag(name = "Custom Food", description = "The Custom Food API provides operations for interacting with custom foods.")
public interface CustomFoodController {

    @Operation(summary = "Get all custom foods", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = NotCompleteFoodView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = InvalidUserTokenHeaderException.class)))})
    @GetMapping(path = "/all")
    List<NotCompleteFoodView> getAllCustomFoods(
            @Parameter(description = "User token") @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidUserTokenHeaderException;

    @Operation(summary = "Search all embedded foods by description", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = NotCompleteFoodView.class))),
            @ApiResponse(responseCode = "400", description = "Food Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @GetMapping(path = "/search")
    List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(
            @Parameter(description = "Description") @RequestParam String description,
            @Parameter(description = "User token") @RequestHeader(name = "X-ViewUser") String userToken)
            throws FoodException, InvalidUserTokenHeaderException;

    @Operation(summary = "Get custom food by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CustomFoodView.class))),
            @ApiResponse(responseCode = "400", description = "Food Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @GetMapping(path = "/{id}")
    CustomFoodView getCustomFoodById(
            @Parameter(description = "User token") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Id") @PathVariable String id)
            throws FoodException, InvalidUserTokenHeaderException;

    @Operation(summary = "Add food", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Food Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @PostMapping
    ResponseEntity<Void> addFood(
            @Valid @RequestBody CreateCustomFood createCustomFood,
            BindingResult bindingResult,
            @Parameter(description = "User token") @RequestHeader(name = "X-ViewUser") String userToken)
            throws FoodException, InvalidUserTokenHeaderException;

    @Operation(summary = "Delete custom food", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Food Exception or Invalid User Token Header",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @DeleteMapping(path = "{id}")
    void deleteCustomFood(
            @Parameter(description = "User token") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Id") @PathVariable(name = "id") String id)
            throws FoodException, InvalidUserTokenHeaderException;

    @Operation(summary = "Calculate food", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CustomFoodView.class))),
            @ApiResponse(responseCode = "400", description = "Food Exception",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @PatchMapping("/calculate")
    public CustomFoodView calculateFood(
            @RequestBody CustomFoodView food,
            @Parameter(description = "User token") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Amount") @RequestParam(name = "amount") Double amount) throws FoodException;
}