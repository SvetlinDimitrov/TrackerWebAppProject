package org.food.domain.dtos.foodView.storageFoodView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class FoodPortionView {

    private String name;
    private String modifier;
    private BigDecimal gramWeight;
    private BigDecimal amount;

}
