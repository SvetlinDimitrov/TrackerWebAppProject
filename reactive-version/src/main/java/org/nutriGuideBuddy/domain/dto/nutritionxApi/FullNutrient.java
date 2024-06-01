package org.nutriGuideBuddy.domain.dto.nutritionxApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FullNutrient {

  @JsonProperty("attr_id")
  private Integer tag;

  @JsonProperty("value")
  private BigDecimal value;
}
