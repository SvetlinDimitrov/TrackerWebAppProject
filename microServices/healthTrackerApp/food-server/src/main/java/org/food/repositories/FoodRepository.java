package org.food.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.food.domain.dtos.Food;
import org.springframework.stereotype.Repository;

@Repository
public class FoodRepository {
    private final HashMap<String, Food> foodRepo = new HashMap<>();

    public List<Food> getAllFoods() {
        return new ArrayList<>(foodRepo.values());
    }

    public List<String> getAllNames() {
        return foodRepo.values().stream().map(Food::getName).collect(Collectors.toList());
    }

    public Optional<Food> getByName(String name) {
        return Optional.ofNullable(foodRepo.get(name));
    }

    public void addFood(Food food) {
        foodRepo.put(food.getName(), food);
    }
}
