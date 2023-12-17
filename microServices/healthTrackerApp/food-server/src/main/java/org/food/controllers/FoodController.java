package org.food.controllers;

import java.util.List;

import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.Food;
import org.food.domain.dtos.SingleErrorResponse;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping(path = "/api/food")
@RestController
@Tag(name = "Custom Food", description = "The Custom Food API")
public interface FoodController {

    @PostMapping
    @Operation(summary = "Add a custom food", responses = {
            @ApiResponse(responseCode = "201", description = "Custom food added", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))

    }, requestBody = @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCustomFood.class), examples = @ExampleObject(name = "createCustomFood", value = "{\"name\":\"Custom Apple\",\"size\":100,\"calories\":52,\"a\":0.027,\"d\":0,\"e\":0.18,\"k\":2.6,\"c\":4.6,\"b1\":0.017,\"b2\":0.026,\"b3\":0.091,\"b5\":0.061,\"b6\":0.041,\"b7\":0.002,\"b9\":0.003,\"b12\":0,\"calcium\":6,\"phosphorus\":11,\"magnesium\":5,\"sodium\":1,\"potassium\":107,\"chloride\":0,\"iron\":0.12,\"zinc\":0.04,\"copper\":0.027,\"manganese\":0.035,\"iodine\":0,\"selenium\":0,\"fluoride\":0,\"chromium\":0,\"molybdenum\":0,\"carbohydrates\":13.81,\"protein\":0.26,\"fat\":0.17,\"fiber\":2.4,\"transFat\":0,\"saturatedFat\":0.028,\"sugar\":10.39,\"polyunsaturatedFat\":0.05,\"monounsaturatedFat\":0.007}"))))
    ResponseEntity<HttpStatus> addFood(
            @Valid @RequestBody CreateCustomFood createCustomFood,
            BindingResult bindingResult,
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws FoodException, InvalidUserTokenHeaderException;

    @GetMapping(path = "/all")
    @Operation(summary = "Get all custom foods", responses = {
            @ApiResponse(responseCode = "200", description = "List of all custom foods", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Food.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
    })
    ResponseEntity<List<Food>> getAllCustomFoods(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidUserTokenHeaderException;

    @Operation(summary = "Get a custom food by name", responses = {
            @ApiResponse(responseCode = "200", description = "Custom food details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Food.class))),
            @ApiResponse(responseCode = "400", description = "Custom food not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
    })
    @GetMapping
    ResponseEntity<Food> getCustomFood(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Name of the custom food to be obtained.", example = "Custom Apple") @RequestParam(name = "foodName") String name,
            @Parameter(description = "Amount of the custom food to be obtained.", example = "100") @RequestParam(name = "amount", required = false) Double amount)
            throws FoodException, InvalidUserTokenHeaderException;

    @Operation(summary = "Delete a custom food", responses = {
            @ApiResponse(responseCode = "200", description = "Custom food deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Custom food not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
    })
    @DeleteMapping
    ResponseEntity<HttpStatus> deleteCustomFood(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Name of the custom food to be deleted.", example = "Custom Apple") @RequestParam(name = "foodName") String name)
            throws FoodException, InvalidUserTokenHeaderException;

    @PatchMapping("/calculate")
    public ResponseEntity<Food> getMethodName(@org.springframework.web.bind.annotation.RequestBody Food food,
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "amount") Double amount) throws FoodException;

}
