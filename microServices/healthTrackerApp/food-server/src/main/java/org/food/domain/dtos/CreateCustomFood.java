package org.food.domain.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.domain.entity.storageEntity.Nutrient;

import java.math.BigDecimal;
import java.util.List;

@Setter
@NoArgsConstructor
@Getter
public class CreateCustomFood {

    private String description;
    private BigDecimal calories;
    private BigDecimal size;
    private List<Nutrient> vitaminNutrients;
    private List<Nutrient> macronutrients;
    private List<Nutrient> mineralNutrients;

}
