package org.record.model.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NutritionIntake {

    private String nutrientName;
    private String nutrientType;
    private BigDecimal dailyConsumed ;
    private BigDecimal lowerBoundIntake ;
    private BigDecimal upperBoundIntake ;
    private String measurement;

}
