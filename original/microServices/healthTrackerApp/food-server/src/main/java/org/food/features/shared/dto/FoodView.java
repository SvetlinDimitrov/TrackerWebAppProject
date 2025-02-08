package org.food.features.shared.dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FoodView {

    private String id;
    private String description;
    private String foodClass;
    private CaloriesView calories;
    private String measurement;
    private BigDecimal size;
    private List<NutrientView> vitaminNutrients = new ArrayList<>();
    private List<NutrientView> macroNutrients = new ArrayList<>();
    private List<NutrientView> mineralNutrients = new ArrayList<>();

}
