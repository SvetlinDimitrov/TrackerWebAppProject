package org.storage.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.storage.model.entity.Calories;

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

    public Calories toCalories () {
        Calories calories = new Calories();
        calories.setName(name);
        calories.setAmount(amount);
        calories.setUnit(unit);
        return calories;
    }
}
