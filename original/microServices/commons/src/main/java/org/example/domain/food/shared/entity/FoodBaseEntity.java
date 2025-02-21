package org.example.domain.food.shared.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "foods")
public class FoodBaseEntity {

  @Id
  private String id;
  private String description;
  private String foodClass;
  private Calories calories;
  private String measurement;
  private BigDecimal size;
  private List<Nutrient> vitaminNutrients = new ArrayList<>();
  private List<Nutrient> macroNutrients = new ArrayList<>();
  private List<Nutrient> mineralNutrients = new ArrayList<>();
}
