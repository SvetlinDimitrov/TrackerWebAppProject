package org.record.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.record.client.NutritionIntakeView;
import org.record.client.StorageView;

import java.math.BigDecimal;
import java.util.List;


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
