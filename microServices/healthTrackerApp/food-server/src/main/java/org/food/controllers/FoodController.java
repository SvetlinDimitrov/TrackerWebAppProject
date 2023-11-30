package org.food.controllers;

import java.util.List;

import org.food.domain.dtos.ErrorResponse;
import org.food.domain.dtos.Food;
import org.food.exception.FoodNotFoundException;
import org.food.services.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        return new ResponseEntity<>(foodService.getAllFoods(), HttpStatus.OK);
    }

    @GetMapping("/{foodName}")
    public ResponseEntity<Food> getFoodByName(
            @PathVariable String foodName,
            @RequestParam(required = false) Double amount) throws FoodNotFoundException {
        return new ResponseEntity<>(foodService.getFoodByName(foodName, amount), HttpStatus.OK);
    }

    @ExceptionHandler(FoodNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchMacroNotFoundError(FoodNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getFoodsList());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
