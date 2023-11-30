package org.food.services;

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
        return foodRepository.getAllFoods()
                .stream()
                .collect(Collectors.toList());
    }

    public Food getFoodByName(String foodName, Double amount) throws FoodNotFoundException {

        Food food = foodRepository
                .getByName(foodName)
                .orElseThrow(
                        () -> new FoodNotFoundException(foodName + " does not exist.", foodRepository.getAllNames()));

        if (amount == null) {
            return food;
        }

        return calculateFoodByAmount(food, amount);
    }

}
