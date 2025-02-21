package org.example.domain.food.shared.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodView {

  private String id;
  private String description;
  private String foodClass;
  private CaloriesView calories;
  private String measurement;
  private BigDecimal size;
  private List<NutrientView> vitaminNutrients = new ArrayList<>();
  private List<NutrientView> macroNutrients = new ArrayList<>();
  private List<NutrientView> mineralNutrients = new ArrayList<>();

}
