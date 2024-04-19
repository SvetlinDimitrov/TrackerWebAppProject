package org.food.domain.dtos.foodView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.domain.dtos.foodView.storageFoodView.CaloriesView;
import org.food.domain.dtos.foodView.storageFoodView.NutrientView;

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
    private List<NutrientView> vitaminNutrients;
    private List<NutrientView> macroNutrients;
    private List<NutrientView> mineralNutrients;

}
