package org.food.domain.dtos.foodView.storageFoodView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CaloriesView {
    private String name;
    private BigDecimal amount;
    private String unit;

    public CaloriesView(BigDecimal amount) {
        this.name = "Energy";
        this.amount = amount;
        this.unit = "kcal";
    }
}
