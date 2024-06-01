package org.nutriGuideBuddy.domain.dto.record;

import lombok.Data;
import org.nutriGuideBuddy.domain.dto.NutritionIntakeView;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RecordView {

  private BigDecimal dailyCaloriesToConsume;
  private BigDecimal dailyCaloriesConsumed;
  private List<NutritionIntakeView> vitaminIntake;
  private List<NutritionIntakeView> mineralIntakes;
  private List<NutritionIntakeView> macroIntakes;
}
