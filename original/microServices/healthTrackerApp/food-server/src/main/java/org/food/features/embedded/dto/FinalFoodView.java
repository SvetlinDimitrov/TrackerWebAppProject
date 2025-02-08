package org.food.features.embedded.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.features.shared.dto.FoodView;
import org.food.features.shared.dto.FoodPortionView;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FinalFoodView extends FoodView {
    private List<FoodPortionView> foodPortions;
    private String foodGroups;
}
