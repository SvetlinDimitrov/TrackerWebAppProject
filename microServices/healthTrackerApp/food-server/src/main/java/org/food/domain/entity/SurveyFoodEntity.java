package org.food.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.food.domain.entity.storageEntity.FoodPortion;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
public class SurveyFoodEntity extends FoodBaseEntity{
    private List<FoodPortion> foodPortions;
}
