package org.food.features.embedded.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.features.shared.dto.FoodPortionView;

import java.util.List;
import org.food.features.shared.dto.FoodView;

@Getter
@Setter
@NoArgsConstructor
public class SurveyFoodView extends FoodView {
    private List<FoodPortionView> foodPortions;
}
