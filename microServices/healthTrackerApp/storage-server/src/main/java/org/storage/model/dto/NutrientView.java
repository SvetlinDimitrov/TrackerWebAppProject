package org.storage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.storage.model.entity.Nutrient;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutrientView {

    private String name;
    private String unit;
    private BigDecimal amount;

    public Nutrient toNutrient() {
        Nutrient nutrient = new Nutrient();
        nutrient.setName(name);
        nutrient.setUnit(unit);
        nutrient.setAmount(amount);
        return nutrient;
    }

}
