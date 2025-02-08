package org.food.features.embedded.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.features.shared.dto.FoodView;

@Getter
@Setter
@NoArgsConstructor
public class BrandedFoodView extends FoodView {
    private BrandedInfoView brandedInfo;
    private String foodGroups;
}
