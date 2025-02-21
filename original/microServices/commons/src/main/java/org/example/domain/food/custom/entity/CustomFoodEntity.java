package org.example.domain.food.custom.entity;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.food.shared.entity.FoodBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "foods")
public class CustomFoodEntity extends FoodBaseEntity {

  private UUID userId;

  public CustomFoodEntity() {
    super();
    setFoodClass("Custom");
    setMeasurement("g");
  }
}
