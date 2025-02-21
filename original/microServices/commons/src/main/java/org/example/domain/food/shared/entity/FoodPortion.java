package org.example.domain.food.shared.entity;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodPortion {

  private String name;
  private String modifier;
  private BigDecimal gramWeight;
  private BigDecimal amount;

}
