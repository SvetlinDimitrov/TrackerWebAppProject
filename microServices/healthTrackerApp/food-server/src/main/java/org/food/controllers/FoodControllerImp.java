package org.food.controllers;

import java.util.List;

import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.Food;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.food.services.CustomFoodServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FoodControllerImp implements FoodController {

    private final CustomFoodServiceImp foodService;

    @Override
    public ResponseEntity<HttpStatus> addFood(
            @Valid @RequestBody CreateCustomFood createCustomFood,
            BindingResult bindingResult,
            String userToken) throws FoodException, InvalidUserTokenHeaderException {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        foodService.addCustomFood(createCustomFood, userToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<Food>> getAllCustomFoods(String userToken) throws InvalidUserTokenHeaderException {

        List<Food> foods = foodService.getAllCustomFoods(userToken);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Food> getCustomFood(String userToken, String name, Double amount)
            throws FoodException, InvalidUserTokenHeaderException {
        Food food = foodService.getCustomFoodByNameAndUserId(name, userToken, amount);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteCustomFood(String userToken, String name)
            throws FoodException, InvalidUserTokenHeaderException {
        foodService.deleteCustomFood(name, userToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Food> getMethodName(Food food, String userToken, Double amount) throws FoodException {

        return new ResponseEntity<>(foodService.calculateNutrients(food, amount), HttpStatus.OK);
    }
}