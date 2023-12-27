package org.food.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.domain.entity.storageEntity.Calories;
import org.food.domain.entity.storageEntity.FoodPortion;
import org.food.domain.entity.storageEntity.Nutrient;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FoodBaseEntity {
    @Id
    private String id;
    private String description;
    private String foodClass;
    private Calories calories;
    private String measurement;
    private BigDecimal size;
    private List<Nutrient> vitaminNutrients;
    private List<Nutrient> macronutrients;
    private List<Nutrient> mineralNutrients;
}
