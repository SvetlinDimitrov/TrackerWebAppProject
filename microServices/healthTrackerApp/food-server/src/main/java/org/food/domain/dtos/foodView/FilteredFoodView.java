package org.food.domain.dtos.foodView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilteredFoodView {
    private String id;
    private String description;
    private String nutrient;
    private BigDecimal amount;
    private String unit;

}
