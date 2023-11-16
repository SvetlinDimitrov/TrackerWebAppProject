package org.food;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.food.domain.dtos.FoodView;
import org.food.repository.FruitRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FruitRepository fruitRepository;

    public List<FoodView> getAllFoods() {
        return fruitRepository.getAllFoods()
                .stream()
                .map(FoodView::new)
                .collect(Collectors.toList());
    }

    public FoodView getFoodByName(String foodName) throws FoodNotFoundException {
        return fruitRepository
                .getByName(foodName)
                .map(FoodView::new)
                .orElseThrow(
                        () -> new FoodNotFoundException(foodName + " does not exist.", fruitRepository.getAllNames()));
    }

    public List<FoodView> getAllFoodsByListNames(List<String> foodNames) throws FoodNotFoundException {
        List<FoodView> foodsToReturn = new ArrayList<>();

        for (String foodName : foodNames) {
            foodsToReturn.add(getFoodByName(foodName));
        }

        return foodsToReturn;
    }
}
