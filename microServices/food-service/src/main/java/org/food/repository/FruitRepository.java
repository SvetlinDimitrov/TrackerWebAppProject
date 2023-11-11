package org.food.repository;

import jakarta.annotation.PostConstruct;
import org.food.domain.entity.Food;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FruitRepository {

    HashMap<String , Food> fruitRepo = new HashMap<>();

    public List<Food> getAllFoods () {
        return new ArrayList<>(fruitRepo.values());
    }

    public List<String> getAllNames (){
        return fruitRepo.values().stream().map(Food::getName).collect(Collectors.toList());
    }

    public Optional<Food> getByName (String name) {
        return Optional.ofNullable(fruitRepo.get(name));
    }

    @PostConstruct
    private void initData (){
        fruitRepo.put("Apple" ,
                Food.builder()
                        .name("Apple")
                        .measurement("grams")
                        .size(new BigDecimal("100"))
                        .calories(new BigDecimal("52"))
                        .A(new BigDecimal("0"))
                        .C(new BigDecimal("0.5"))
                        .E(new BigDecimal("0.2"))
                        .K(new BigDecimal("2.2"))
                        .B1(new BigDecimal("0.0"))
                        .B2(new BigDecimal("0.0"))
                        .B3(new BigDecimal("0.1"))
                        .B5(new BigDecimal("0.1"))
                        .B6(new BigDecimal("0.0"))
                        .B9(new BigDecimal("1.0"))
                        .B12(new BigDecimal("0.0"))

                        .Calcium(new BigDecimal("6"))
                        .Magnesium(new BigDecimal("6"))
                        .Potassium(new BigDecimal("107"))
                        .Sodium(new BigDecimal("1"))
                        .Chloride(new BigDecimal("0"))

                        .Fat(new BigDecimal("0.2"))
                        .Protein(new BigDecimal("0.3"))
                        .Carbohydrates(new BigDecimal("14"))
                        .build());
    }
}
