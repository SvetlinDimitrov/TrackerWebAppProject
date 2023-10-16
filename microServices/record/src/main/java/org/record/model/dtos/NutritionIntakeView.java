package org.record.model.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NutritionIntakeView {

    private Long id;
    private String nutrientName;
    private String nutrientType;
    private BigDecimal lowerBoundIntake;
    private BigDecimal upperBoundIntake;
    private BigDecimal dailyConsumed;
    private String measurement;
    private Long recordId;


}
