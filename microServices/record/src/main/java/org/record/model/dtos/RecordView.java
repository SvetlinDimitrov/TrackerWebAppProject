package org.record.model.dtos;

import java.math.BigDecimal;
import java.util.List;

import org.record.client.dto.NutritionIntakeView;
import org.record.client.dto.StorageView;

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
    private List<NutritionIntakeView> dailyIntakeViews;
    private List<StorageView> storageViews;
    private BigDecimal dailyCaloriesToConsume;
    private BigDecimal dailyConsumedCalories;
    private Long userID;
    private String date;
}
