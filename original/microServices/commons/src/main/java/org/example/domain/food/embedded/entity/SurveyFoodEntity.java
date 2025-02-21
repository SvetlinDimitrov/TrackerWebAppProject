package org.example.domain.food.embedded.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.food.shared.entity.FoodBaseEntity;
import org.example.domain.food.shared.entity.FoodPortion;

@Getter
@Setter
public class SurveyFoodEntity extends FoodBaseEntity {
    private List<FoodPortion> foodPortions;
}
