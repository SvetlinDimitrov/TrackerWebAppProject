package org.food.domain.dtos.foodView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CustomFoodView extends FoodView {
    private Long userId;
}
