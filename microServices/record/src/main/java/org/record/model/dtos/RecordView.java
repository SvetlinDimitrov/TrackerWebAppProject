package org.record.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.record.client.NutritionIntakeView;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordView {

    private Long id;
    private List<NutritionIntakeView> dailyIntakeViews;
    private BigDecimal dailyCaloriesToConsume;
    private Long userID;


}
