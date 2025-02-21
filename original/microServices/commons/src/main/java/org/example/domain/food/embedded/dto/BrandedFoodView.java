package org.example.domain.food.embedded.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.food.shared.dto.FoodView;

@Getter
@Setter
@NoArgsConstructor
public class BrandedFoodView extends FoodView {

  private BrandedInfoView brandedInfo;
  private String foodGroups;
}
