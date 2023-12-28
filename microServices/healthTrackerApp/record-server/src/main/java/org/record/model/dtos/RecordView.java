package org.record.model.dtos;

import java.math.BigDecimal;
import java.util.List;

import org.record.client.dto.StorageView;
import org.record.model.entity.NutritionIntake;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RecordView {

    private String id;
    private List<StorageView> storageViews;
    private List<NutritionIntake> nutritionIntakesViews;
    private BigDecimal dailyCaloriesToConsume;
    private BigDecimal dailyConsumedCalories;
    private String userID;
    private String name;
    private String date;
}
