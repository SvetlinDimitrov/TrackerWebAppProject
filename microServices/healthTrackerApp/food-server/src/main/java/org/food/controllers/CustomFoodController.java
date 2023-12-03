package org.food.controllers;

import java.util.List;

import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.Food;
import org.food.domain.dtos.SingleErrorResponse;
import org.food.exception.FoodException;
import org.food.services.CustomFoodServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/api/food/customFood")
@RequiredArgsConstructor
public class CustomFoodController {

    private final CustomFoodServiceImp foodService;

    @PostMapping
    public ResponseEntity<HttpStatus> addFood(
            @Valid @RequestBody CreateCustomFood createCustomFood,
            BindingResult bindingResult,
            @RequestHeader(name = "X-ViewUser") String userToken) throws FoodException {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        foodService.addCustomFood(createCustomFood, userToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Food>> getAllCustomFoods(
            @RequestHeader(name = "X-ViewUser") String userToken) {
        List<Food> foods = foodService.getAllCustomFoods(userToken);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Food> getCustomFood(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "foodName") String name,
            @RequestParam(name = "amount", required = false) Double amount) throws FoodException {
        Food food = foodService.getCustomFoodByNameAndUserId(name, userToken, amount);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteCustomFood(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "foodName") String name) throws FoodException {
        foodService.deleteCustomFood(name, userToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(FoodException.class)
    public ResponseEntity<SingleErrorResponse> handleFoodException(FoodException e) {
        return new ResponseEntity<>(new SingleErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
