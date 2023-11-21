package org.record.model.dtos;

import java.math.BigDecimal;
import java.util.List;

import org.record.client.dto.StorageView;
import org.record.model.entity.NutritionIntake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordView {

    private Long id;
    private List<StorageView> storageViews;
    private List<NutritionIntake> nutritionIntakesViews;
    private BigDecimal dailyCaloriesToConsume;
    private BigDecimal dailyConsumedCalories;
    private Long userID;
    private String name;
    private String date;
}
