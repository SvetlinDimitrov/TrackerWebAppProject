package org.record.features.storage.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.food.custom.entity.CustomFoodEntity;
import org.example.domain.food.shared.entity.FoodBaseEntity;
import org.record.features.record.entity.Record;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "storages")
public class Storage {

  @Id
  private String id;
  private String name;
  private Record record;
  private BigDecimal consumedCalories;
  private Map<String, CustomFoodEntity> foods;
}
