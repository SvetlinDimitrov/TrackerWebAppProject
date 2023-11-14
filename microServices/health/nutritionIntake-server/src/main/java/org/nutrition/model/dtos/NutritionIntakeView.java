package org.nutrition.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionIntakeView {

    private Long id;
    private String nutrientName;
    private String nutrientType;
    private BigDecimal dailyConsumed = BigDecimal.ZERO;
    private BigDecimal lowerBoundIntake;
    private BigDecimal upperBoundIntake;
    private String measurement;
    private Long recordId;

}
