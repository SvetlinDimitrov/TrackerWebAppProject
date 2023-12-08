package org.food.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.food.clients.dto.User;
import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.Food;
import org.food.domain.entity.CustomFood;
import org.food.exception.FoodException;
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

    public void addCustomFood(CreateCustomFood customFood, String userToken) throws FoodException {
        Long userId = getUserId(userToken);
        CustomFood food = toCustomFood(customFood);

        if (customFoodRepository.findByNameAndUserId(customFood.getName() , userId).isPresent()) {
            throw new FoodException("Food with name " + customFood.getName() + " already exists.");
        }

        food.setUserId(userId);
        customFoodRepository.save(food);
    }

    public void deleteCustomFood(String name, String userToken) throws FoodException {
        Long userId = getUserId(userToken);

        CustomFood food = customFoodRepository
                .findByNameAndUserId(name, userId)
                .orElseThrow(() -> new FoodException("Food with name " + name + " does not exist."));

        customFoodRepository.delete(food);
    }

    public List<Food> getAllCustomFoods(String userToken) {
        Long userId = getUserId(userToken);
        return customFoodRepository
                .findAll()
                .stream()
                .filter(food -> food.getUserId().equals(userId))
                .map(food -> modelMapper.map(food, Food.class))
                .collect(Collectors.toList());
    }

    public Food getCustomFoodByNameAndUserId(String name, String userToken, Double amount) throws FoodException {
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

    private Long getUserId(String userToken) {
        return gsonWrapper.fromJson(userToken, User.class).getId();
    }
    
    private Food generateEmptyFood(String name , Long foodId){
        Food baseFood = new Food();
        baseFood.setName(name);
        baseFood.setId(foodId);
        baseFood.setSize(BigDecimal.ZERO);
        baseFood.setCalories(BigDecimal.ZERO);
        
        baseFood.setA(BigDecimal.ZERO);
        baseFood.setD(BigDecimal.ZERO);
        baseFood.setE(BigDecimal.ZERO);
        baseFood.setK(BigDecimal.ZERO);
        baseFood.setC(BigDecimal.ZERO);
        baseFood.setB1(BigDecimal.ZERO);
        baseFood.setB2(BigDecimal.ZERO);
        baseFood.setB3(BigDecimal.ZERO);
        baseFood.setB5(BigDecimal.ZERO);
        baseFood.setB6(BigDecimal.ZERO);
        baseFood.setB7(BigDecimal.ZERO);
        baseFood.setB9(BigDecimal.ZERO);
        baseFood.setB12(BigDecimal.ZERO);
        
        baseFood.setCalcium(BigDecimal.ZERO);
        baseFood.setPhosphorus(BigDecimal.ZERO);
        baseFood.setMagnesium(BigDecimal.ZERO);
        baseFood.setSodium(BigDecimal.ZERO);
        baseFood.setPotassium(BigDecimal.ZERO);
        baseFood.setChloride(BigDecimal.ZERO);
        baseFood.setIron(BigDecimal.ZERO);
        baseFood.setZinc(BigDecimal.ZERO);
        baseFood.setCopper(BigDecimal.ZERO);
        baseFood.setManganese(BigDecimal.ZERO);
        baseFood.setIodine(BigDecimal.ZERO);
        baseFood.setSelenium(BigDecimal.ZERO);
        baseFood.setFluoride(BigDecimal.ZERO);
        baseFood.setChromium(BigDecimal.ZERO);
        baseFood.setMolybdenum(BigDecimal.ZERO);
        
        baseFood.setCarbohydrates(BigDecimal.ZERO);
        baseFood.setProtein(BigDecimal.ZERO);
        baseFood.setFat(BigDecimal.ZERO);
        
        baseFood.setFiber(BigDecimal.ZERO);
        baseFood.setTransFat(BigDecimal.ZERO);
        baseFood.setSaturatedFat(BigDecimal.ZERO);
        baseFood.setSugar(BigDecimal.ZERO);
        baseFood.setPolyunsaturatedFat(BigDecimal.ZERO);
        baseFood.setMonounsaturatedFat(BigDecimal.ZERO);
        return baseFood;
    }

}
