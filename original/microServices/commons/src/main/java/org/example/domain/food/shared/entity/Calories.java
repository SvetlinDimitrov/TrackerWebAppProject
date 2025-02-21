package org.example.domain.food.shared.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Calories {

    private String name;
    private BigDecimal amount;
    private String unit;

    public Calories(BigDecimal amount) {
        this.name = "Energy";
        this.amount = amount;
        this.unit = "kcal";
    }
}
