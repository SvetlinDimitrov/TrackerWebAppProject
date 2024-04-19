package org.trackerwebapp.shared_interfaces.domain.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NutritionIntakeView {

  private String name;
  private BigDecimal dailyConsumed;
  private BigDecimal recommendedIntake;
  private String measurement;
}
