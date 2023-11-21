package org.food.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.food.Food;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class FruitRepository {

    HashMap<String, Food> fruitRepo = new HashMap<>();

    public List<Food> getAllFoods() {
        return new ArrayList<>(fruitRepo.values());
    }

    public List<String> getAllNames() {
        return fruitRepo.values().stream().map(Food::getName).collect(Collectors.toList());
    }

    public Optional<Food> getByName(String name) {
        return Optional.ofNullable(fruitRepo.get(name));
    }

    @PostConstruct
    private void initData() {
        Food apple = Food.builder()
        .name("Apple")
        .calories(new BigDecimal("52"))
        .A(new BigDecimal("0"))
        .D(new BigDecimal("0"))
        .E(new BigDecimal("0.18"))
        .K(new BigDecimal("0.5"))
        .C(new BigDecimal("0.5"))
        .B1(new BigDecimal("0.017"))
        .B2(new BigDecimal("0.026"))
        .B3(new BigDecimal("0.091"))
        .B5(new BigDecimal("0.043"))
        .B6(new BigDecimal("0.041"))
        .B7(new BigDecimal("0.009"))
        .B9(new BigDecimal("0.003"))
        .B12(new BigDecimal("0"))
        .Calcium(new BigDecimal("6"))
        .Phosphorus(new BigDecimal("11"))
        .Magnesium(new BigDecimal("5"))
        .Sodium(new BigDecimal("1"))
        .Potassium(new BigDecimal("107"))
        .Chloride(new BigDecimal("0"))
        .Iron(new BigDecimal("0.12"))
        .Zinc(new BigDecimal("0.05"))
        .Copper(new BigDecimal("0.05"))
        .Manganese(new BigDecimal("0.035"))
        .Iodine(new BigDecimal("0"))
        .Selenium(new BigDecimal("0"))
        .Fluoride(new BigDecimal("0"))
        .Chromium(new BigDecimal("0"))
        .Molybdenum(new BigDecimal("0"))
        .Carbohydrates(new BigDecimal("14"))
        .Protein(new BigDecimal("0.3"))
        .Fat(new BigDecimal("0.2"))
        .build();

        fruitRepo.put(apple.getName(), apple);
    }
}
