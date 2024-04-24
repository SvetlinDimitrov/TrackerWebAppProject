package org.trackerwebapp.trackerwebapp.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class NutritionIntakeView {

  private String name;
  private BigDecimal dailyConsumed;
  private BigDecimal recommendedIntake;
  private String measurement;
}
