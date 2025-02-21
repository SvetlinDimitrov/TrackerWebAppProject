package org.example.domain.food.embedded.entity;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BrandedInfo {

  private String ingredients;
  private String brandOwner;
  private String marketCountry;
  private BigDecimal servingSize;
  private String servingSizeUnit;
}
