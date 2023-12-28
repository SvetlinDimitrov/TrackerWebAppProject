package org.storage.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.storage.model.entity.Food;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
public class FoodInsertDto {

    private String id;
    private String description;
    private String foodClass;
    private CaloriesView calories;
    private String measurement;
    private BigDecimal size;
    private List<NutrientView> vitaminNutrients;
    private List<NutrientView> macronutrients;
    private List<NutrientView> mineralNutrients;

    public Food toFood() {
        Food food = new Food();
        food.setId(id);
        food.setDescription(description);
        food.setFoodClass(foodClass);
        food.setCalories(calories.toCalories());
        food.setMeasurement(measurement);
        food.setSize(size);
        food.setVitaminNutrients(vitaminNutrients.stream().map(NutrientView::toNutrient).toList());
        food.setMacronutrients(macronutrients.stream().map(NutrientView::toNutrient).toList());
        food.setMineralNutrients(mineralNutrients.stream().map(NutrientView::toNutrient).toList());
        return food;
    }

}
