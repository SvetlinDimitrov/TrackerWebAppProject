package org.food;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.food.repositories.FoodRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public List<Food> getAllFoods() {
        return foodRepository.getAllFoods()
                .stream()
                .collect(Collectors.toList());
    }

    public Food getFoodByName(String foodName) throws FoodNotFoundException {
        return foodRepository
                .getByName(foodName)
                .orElseThrow(
                        () -> new FoodNotFoundException(foodName + " does not exist.", foodRepository.getAllNames()));
    }

    public List<Food> getAllFoodsByListNames(List<String> foodNames) throws FoodNotFoundException {
        List<Food> foodsToReturn = new ArrayList<>();

        for (String foodName : foodNames) {
            foodsToReturn.add(getFoodByName(foodName));
        }

        return foodsToReturn;
    }
}
