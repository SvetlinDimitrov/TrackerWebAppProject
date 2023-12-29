package org.food.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "foods")
public class CustomFoodEntity extends FoodBaseEntity{
    private String userId;
}
