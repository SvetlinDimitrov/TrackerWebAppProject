package org.example.domain.food.custom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.food.shared.dto.FoodView;

@Setter
@Getter
@NoArgsConstructor
public class CustomFoodView extends FoodView {
    private String userId;
}
