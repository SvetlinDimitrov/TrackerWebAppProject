package org.nutriGuideBuddy.domain.dto.nutritionxApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListFoodsResponse {

  private List<CommandFoodShortenDto> common;
  private List<BrandedFoodShortenDto> branded;
}
