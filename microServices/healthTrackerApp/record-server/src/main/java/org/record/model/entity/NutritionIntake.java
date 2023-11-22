package org.record.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@Builder
public class NutritionIntake {

    private String nutrientName;
    private String nutrientType;
    private BigDecimal dailyConsumed = BigDecimal.ZERO;
    private BigDecimal lowerBoundIntake = BigDecimal.ZERO;
    private BigDecimal upperBoundIntake = BigDecimal.ZERO;
    private String measurement;

}
