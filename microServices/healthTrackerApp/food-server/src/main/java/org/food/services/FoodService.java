package org.food.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.food.domain.dtos.Food;
import org.food.exception.FoodNotFoundException;
import org.food.repositories.FoodRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService extends AbstractFoodService {

    private final FoodRepository foodRepository;

    public List<Food> getAllFoods() {
        return new ArrayList<>(foodRepository.getAllFoods());
    }

    public Food getFoodByName(String foodName, Double amount) throws FoodNotFoundException {

        Food food = foodRepository
                .getByName(foodName)
                .orElseThrow(
                        () -> new FoodNotFoundException(foodName + " does not exist.", foodRepository.getAllNames()));

        if (amount == null) {
            return food;
        }
        
        if(amount.compareTo((double) 0) == 0) {
           return  generateEmptyFood(food.getName() , null);
        }
        
        return calculateFoodByAmount(food, amount);
    }

}
