package org.food.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.ErrorResponse;
import org.food.domain.dtos.Food;
import org.food.domain.dtos.SingleErrorResponse;
import org.food.exception.FoodException;
import org.food.exception.FoodListException;
import org.food.services.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@Tag(name = "Food", description = "The Food API")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "Get all foods", responses = {
            @ApiResponse(responseCode = "200", description = "List of all foods", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Food.class)))
    })
    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        return new ResponseEntity<>(foodService.getAllFoods(), HttpStatus.OK);
    }

    @Operation(summary = "Get food by name", responses = {
            @ApiResponse(responseCode = "200", description = "Food details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Food.class))),
            @ApiResponse(responseCode = "400", description = "Food not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{foodName}")
    public ResponseEntity<Food> getFoodByName(
            @Parameter(description = "Name of the food to be obtained.", example = "Apple") @PathVariable String foodName,
            @Parameter(description = "Amount of the food to be obtained.", example = "100") @RequestParam(required = false) Double amount) throws FoodListException {
        return new ResponseEntity<>(foodService.getFoodByName(foodName, amount), HttpStatus.OK);
    }

    @ExceptionHandler(FoodListException.class)
    public ResponseEntity<ErrorResponse> catchMacroNotFoundError(FoodListException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getAvailableFoodNames());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FoodException.class)
    public ResponseEntity<SingleErrorResponse> catchMacroNotFoundError(FoodException e) {
        SingleErrorResponse errorResponse = new SingleErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}