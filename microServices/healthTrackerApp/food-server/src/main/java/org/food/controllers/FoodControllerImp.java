package org.food.controllers;

import java.util.List;

import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.Food;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.food.services.CustomFoodServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FoodControllerImp implements FoodController {

    private final CustomFoodServiceImp foodService;

    /**
     * Adds a new custom food.
     *
     * @param createCustomFood the details of the food to add
     * @param bindingResult    holds the result of the validation of the
     *                         createCustomFood object
     * @param userToken        the token of the user who is adding the food
     * @return a ResponseEntity with the HTTP status code
     * @throws FoodException                   if there is an error while adding the
     *                                         food
     * @throws InvalidUserTokenHeaderException if the user token is invalid
     */
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

    /**
     * Retrieves all custom foods for a user.
     *
     * @param userToken the token of the user whose foods are to be retrieved
     * @return a ResponseEntity with the list of foods and the HTTP status code
     * @throws InvalidUserTokenHeaderException if the user token is invalid
     */
    @Override
    public ResponseEntity<List<Food>> getAllCustomFoods(String userToken) throws InvalidUserTokenHeaderException {

        List<Food> foods = foodService.getAllCustomFoods(userToken);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    /**
     * Retrieves a custom food by its name and the user's ID.
     *
     * @param userToken the token of the user who owns the food
     * @param name      the name of the food to retrieve
     * @param amount    the amount of the food to retrieve
     * @return a ResponseEntity with the food and the HTTP status code
     * @throws FoodException                   if there is an error while retrieving
     *                                         the food
     * @throws InvalidUserTokenHeaderException if the user token is invalid
     */
    @Override
    public ResponseEntity<Food> getCustomFood(String userToken, String name, Double amount)
            throws FoodException, InvalidUserTokenHeaderException {
        Food food = foodService.getCustomFoodByNameAndUserId(name, userToken, amount);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    /**
     * Deletes a custom food.
     *
     * @param userToken the token of the user who owns the food
     * @param name      the name of the food to delete
     * @return a ResponseEntity with the HTTP status code
     */
    @Override
    public ResponseEntity<HttpStatus> deleteCustomFood(String userToken, String name)
            throws FoodException, InvalidUserTokenHeaderException {
        foodService.deleteCustomFood(name, userToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}