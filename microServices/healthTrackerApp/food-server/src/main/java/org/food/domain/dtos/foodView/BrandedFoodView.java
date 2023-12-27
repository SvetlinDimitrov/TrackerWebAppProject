package org.food.domain.dtos.foodView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.domain.dtos.foodView.storageFoodView.BrandedInfoView;

@Getter
@Setter
@NoArgsConstructor
public class BrandedFoodView extends FoodView{
    private BrandedInfoView brandedInfo;
    private String foodGroups;
}
