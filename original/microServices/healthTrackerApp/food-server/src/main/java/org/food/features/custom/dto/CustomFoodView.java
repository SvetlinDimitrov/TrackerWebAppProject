package org.food.features.custom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.features.shared.dto.FoodView;

@Setter
@Getter
@NoArgsConstructor
public class CustomFoodView extends FoodView {
    private String userId;
}
