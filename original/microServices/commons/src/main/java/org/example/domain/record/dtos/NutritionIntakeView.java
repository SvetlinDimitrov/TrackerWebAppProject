package org.example.domain.record.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionIntakeView {

  private String nutrientName;
  private String nutrientType;
  private BigDecimal dailyConsumed;
  private BigDecimal lowerBoundIntake;
  private BigDecimal upperBoundIntake;
  private String measurement;
}