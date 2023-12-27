package org.food.domain.entity.storageEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class FoodPortion {

    private String name;
    private String modifier;
    private BigDecimal gramWeight;
    private BigDecimal amount;

}
