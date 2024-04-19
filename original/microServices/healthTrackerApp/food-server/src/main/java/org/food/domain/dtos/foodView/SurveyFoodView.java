package org.food.domain.dtos.foodView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.domain.dtos.foodView.storageFoodView.FoodPortionView;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SurveyFoodView extends FoodView {
    private List<FoodPortionView> foodPortions;
}
