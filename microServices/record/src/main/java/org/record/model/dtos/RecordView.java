package org.record.model.dtos;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
public class RecordView {

    private Long id;
//    private List<NutritionIntakeView> dailyIntakeViews;
    private BigDecimal dailyCaloriesToConsume;
    private Long userID;


}
