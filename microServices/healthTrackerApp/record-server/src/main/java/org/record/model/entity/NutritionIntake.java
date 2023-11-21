package org.record.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionIntake {

    private String nutrientName;
    private String nutrientType;
    private BigDecimal dailyConsumed = BigDecimal.ZERO;
    private BigDecimal lowerBoundIntake = BigDecimal.ZERO;
    private BigDecimal upperBoundIntake = BigDecimal.ZERO;
    private String measurement;
        
}
