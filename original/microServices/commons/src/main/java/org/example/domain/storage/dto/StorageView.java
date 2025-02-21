package org.example.domain.storage.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.food.shared.dto.FoodView;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageView {

  private String id;
  private String recordId;
  private String name;
  private BigDecimal consumedCalories;
  private List<FoodView> foods;
}