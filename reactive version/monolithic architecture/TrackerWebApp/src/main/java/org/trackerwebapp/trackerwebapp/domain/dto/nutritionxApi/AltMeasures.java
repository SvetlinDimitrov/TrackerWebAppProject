package org.trackerwebapp.trackerwebapp.domain.dto.nutritionxApi;

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
public class AltMeasures {

  @JsonProperty("serving_weight")
  private BigDecimal servingWeight;
  private String measure;
  private BigDecimal seq;
  private BigDecimal qty;
}
