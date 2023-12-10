package org.food.services;

import java.util.ArrayList;
import java.util.List;

import org.food.domain.dtos.Food;
import org.food.exception.FoodListException;
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

    public Food getFoodByName(String foodName, Double amount) throws FoodListException {

        Food food = foodRepository
                .getByName(foodName)
                .orElseThrow(
                        () -> new FoodListException( foodRepository.getAllNames() , foodName + " does not exist."));

        if (amount == null) {
            return food;
        }
        
        if(amount.compareTo((double) 0) == 0) {
           return  generateEmptyFood(food.getName() , null);
        }
        
        return calculateFoodByAmount(food, amount);
    }

}
