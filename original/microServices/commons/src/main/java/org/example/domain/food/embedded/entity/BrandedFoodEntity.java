package org.example.domain.food.embedded.entity;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.food.shared.entity.FoodBaseEntity;

@Getter
@Setter
public class BrandedFoodEntity extends FoodBaseEntity {
    private BrandedInfo brandedInfo;
    private String foodGroups;
}
