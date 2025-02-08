package org.food.features.embedded.entity;

import lombok.Getter;
import lombok.Setter;
import org.food.features.shared.entity.FoodBaseEntity;

@Getter
@Setter
public class BrandedFoodEntity extends FoodBaseEntity {
    private BrandedInfo brandedInfo;
    private String foodGroups;
}
