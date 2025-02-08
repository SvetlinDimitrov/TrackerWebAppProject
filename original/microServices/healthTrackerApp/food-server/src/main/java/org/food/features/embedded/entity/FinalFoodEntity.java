package org.food.features.embedded.entity;

import lombok.Getter;
import lombok.Setter;
import org.food.features.shared.entity.FoodBaseEntity;
import org.food.features.shared.entity.FoodPortion;

import java.util.List;

@Setter
@Getter
public class FinalFoodEntity extends FoodBaseEntity {
    private List<FoodPortion> foodPortions;
    private String foodGroups;
}
