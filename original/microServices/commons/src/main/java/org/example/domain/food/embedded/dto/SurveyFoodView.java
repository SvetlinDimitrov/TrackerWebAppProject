package org.example.domain.food.embedded.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.food.shared.dto.FoodPortionView;
import org.example.domain.food.shared.dto.FoodView;

@Getter
@Setter
@NoArgsConstructor
public class SurveyFoodView extends FoodView {

  private List<FoodPortionView> foodPortions;
}
