package org.example.domain.food.custom.entity;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.food.shared.entity.FoodBaseEntity;
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
