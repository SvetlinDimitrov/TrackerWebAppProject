package org.food.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.food.domain.entity.storageEntity.BrandedInfo;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "brandedFoods")
public class BrandedFoodEntity extends FoodBaseEntity{
    private BrandedInfo brandedInfo;
    private String foodGroups;
}
