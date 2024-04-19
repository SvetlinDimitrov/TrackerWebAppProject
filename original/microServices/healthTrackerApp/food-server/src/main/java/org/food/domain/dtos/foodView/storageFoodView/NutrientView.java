package org.food.domain.dtos.foodView.storageFoodView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutrientView {

    private String name;
    private String unit;
    private BigDecimal amount;

}
