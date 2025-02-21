package org.example.domain.record.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.storage.dto.StorageView;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordView {

  private String id;
  private List<StorageView> storageViews;
  private List<NutritionIntakeView> nutritionIntakesViews;
  private BigDecimal dailyCaloriesToConsume;
  private BigDecimal dailyConsumedCalories;
  private String name;
  private LocalDateTime date;
}