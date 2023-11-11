package org.record.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.record.client.dto.NutritionIntakeView;
import org.record.client.dto.StorageView;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordView {

    private Long id;
    private List<NutritionIntakeView> dailyIntakeViews;
    private List<StorageView> storageViews;
    private BigDecimal dailyCaloriesToConsume;
    private Long userID;
    private String date;
}
