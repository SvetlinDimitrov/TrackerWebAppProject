package org.food.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.food.clients.dto.User;
import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.Food;
import org.food.domain.entity.CustomFood;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.food.repositories.CustomFoodRepository;
import org.food.utils.GsonWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomFoodServiceImp extends AbstractFoodService {

    private final CustomFoodRepository customFoodRepository;
    private final ModelMapper modelMapper;
    private final GsonWrapper gsonWrapper;

    public void addCustomFood(CreateCustomFood customFood, String userToken) throws FoodException, InvalidUserTokenHeaderException {
        Long userId = getUserId(userToken);
        CustomFood food = toCustomFood(customFood);

        if (customFoodRepository.findByNameAndUserId(customFood.getName() , userId).isPresent()) {
            throw new FoodException("Food with name " + customFood.getName() + " already exists.");
        }

        food.setUserId(userId);
        customFoodRepository.save(food);
    }

    public void deleteCustomFood(String name, String userToken) throws FoodException, InvalidUserTokenHeaderException {
        Long userId = getUserId(userToken);

        CustomFood food = customFoodRepository
                .findByNameAndUserId(name, userId)
                .orElseThrow(() -> new FoodException("Food with name " + name + " does not exist."));

        customFoodRepository.delete(food);
    }

    public List<Food> getAllCustomFoods(String userToken) throws InvalidUserTokenHeaderException {
        Long userId = getUserId(userToken);
        return customFoodRepository
                .findAll()
                .stream()
                .filter(food -> food.getUserId().equals(userId))
                .map(food -> modelMapper.map(food, Food.class))
                .collect(Collectors.toList());
    }

    public Food getCustomFoodByNameAndUserId(String name, String userToken, Double amount) throws FoodException, InvalidUserTokenHeaderException {
        Long userId = getUserId(userToken);

        CustomFood food = customFoodRepository
                .findByNameAndUserId(name, userId)
                .orElseThrow(() -> new FoodException("Food with name " + name + " does not exist."));

        if (amount == null) {
            return modelMapper.map(food, Food.class);
        }
        if (amount.compareTo(0.0) == 0){
            return generateEmptyFood(name , food.getId());
        }
        if(amount.compareTo(0.0) < 0){
            throw new FoodException("Amount cannot be negative.");
        }

        return calculateFoodByAmount(modelMapper.map(food, Food.class), amount);
    }

    private CustomFood toCustomFood(CreateCustomFood customFood) {
        Food food = modelMapper.map(customFood, Food.class);
        Food finalFood = calculateFoodByAmount(food, 100.0);
        return modelMapper.map(finalFood, CustomFood.class);
    }

    private Long getUserId(String userToken) throws InvalidUserTokenHeaderException {
        try{
            return gsonWrapper.fromJson(userToken, User.class).getId();
        } catch (Exception e) {
            throw new InvalidUserTokenHeaderException("Invalid user token header.");
        }
    }
    
   

}
