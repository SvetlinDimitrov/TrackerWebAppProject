package org.food.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.FinalFoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.exception.FoodException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/food/embedded/finalFoods")
@Tag(name = "Final Food", description = "The Final Food API provides operations for interacting with final foods.")
public interface FinalFoodController {

    @Operation(summary = "Get all embedded foods", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = NotCompleteFoodView.class)))})
    @GetMapping(path = "/all")
    List<NotCompleteFoodView> getAllEmbeddedFoods();

    @Operation(summary = "Search all embedded foods by description", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = NotCompleteFoodView.class))),
            @ApiResponse(responseCode = "400", description = "Food Exception",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @GetMapping(path = "/search")
    List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(
            @Parameter(description = "Description") @RequestParam String description) throws FoodException;

    @Operation(summary = "Filter all embedded foods", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = FilteredFoodView.class))),
            @ApiResponse(responseCode = "400", description = "Food Exception",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @GetMapping(path = "/filter")
    List<FilteredFoodView> getAllEmbeddedFoodFilter(
            @Parameter(description = "Nutrient name") @RequestParam String nutrientName,
            @Parameter(description = "Descending order") @RequestParam(defaultValue = "true") Boolean desc,
            @Parameter(description = "Limit") @RequestParam(defaultValue = "50") Integer limit,
            @Parameter(description = "Minimum") @RequestParam(defaultValue = "0.0") Double min,
            @Parameter(description = "Maximum") @RequestParam(defaultValue = "100.0") Double max) throws FoodException;

    @Operation(summary = "Get final food by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = FinalFoodView.class))),
            @ApiResponse(responseCode = "400", description = "Food Exception",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @GetMapping(path = "/{id}")
    FinalFoodView getFinalFoodById(
            @Parameter(description = "Id") @PathVariable String id) throws FoodException;

    @Operation(summary = "Calculate food", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = FinalFoodView.class))),
            @ApiResponse(responseCode = "400", description = "Food Exception",
                    content = @Content(schema = @Schema(implementation = FoodException.class)))})
    @PatchMapping("/calculate")
    public FinalFoodView calculateFood(
            @RequestBody FinalFoodView food,
            @Parameter(description = "Amount") @RequestParam(name = "amount") Double amount) throws FoodException;
}