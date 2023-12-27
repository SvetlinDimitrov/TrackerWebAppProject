package org.food.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.foodView.CustomFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.food.services.CustomFoodServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomFoodControllerImp implements CustomFoodController {

    private final CustomFoodServiceImp foodService;

    @Override
    public List<NotCompleteFoodView> getAllCustomFoods(String userToken) throws InvalidUserTokenHeaderException {
        return foodService.getAllCustomFoods(userToken);
    }

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(String description , String user) throws FoodException, InvalidUserTokenHeaderException {
        return foodService.getAllEmbeddedFoodSearchDescription(description , user);
    }

    @Override
    public CustomFoodView getCustomFood(String userToken, String id)
            throws FoodException, InvalidUserTokenHeaderException {
        return foodService.getCustomFoodByNameAndUserId(id, userToken);
    }

    @Override
    public ResponseEntity<Void> addFood(
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
    public void deleteCustomFood(String userToken, String id)
            throws FoodException, InvalidUserTokenHeaderException {
        foodService.deleteCustomFood(id, userToken);
    }

    @Override
    public CustomFoodView calculateFood(CustomFoodView food, String userToken, Double amount) throws FoodException {
        return foodService.calculateNutrients(food, amount);
    }
}