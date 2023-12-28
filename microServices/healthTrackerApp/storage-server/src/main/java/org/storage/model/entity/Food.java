package org.storage.model.entity;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Food {

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
