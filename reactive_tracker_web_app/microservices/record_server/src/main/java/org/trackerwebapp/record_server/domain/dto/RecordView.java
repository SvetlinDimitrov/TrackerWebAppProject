package org.trackerwebapp.record_server.domain.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.trackerwebapp.shared_interfaces.domain.dto.NutritionIntakeView;

@Data
public class RecordView {

  private BigDecimal dailyCaloriesToConsume;
  private BigDecimal dailyCaloriesConsumed;
  private List<NutritionIntakeView> vitaminIntake;
  private List<NutritionIntakeView> mineralIntakes;
  private List<NutritionIntakeView> macroIntakes;
}
