package org.food.features.custom.entity;

import lombok.Getter;
import lombok.Setter;
import org.food.features.shared.entity.FoodBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "foods")
public class CustomFoodEntity extends FoodBaseEntity {
    private String userId;

    public CustomFoodEntity() {
        super();
        setFoodClass("Custom");
        setMeasurement("g");
    }
}
