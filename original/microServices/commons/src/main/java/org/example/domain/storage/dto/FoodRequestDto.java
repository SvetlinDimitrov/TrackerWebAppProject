package org.example.domain.storage.dto;

import java.math.BigDecimal;
import java.util.List;

import org.example.domain.food.shared.dto.CaloriesView;
import org.example.domain.food.shared.dto.NutrientView;

public record FoodRequestDto(
    String id,
    String description,
    String foodClass,
    CaloriesView calories,
    String measurement,
    BigDecimal size,
    List<NutrientView> vitaminNutrients,
    List<NutrientView> macroNutrients,
    List<NutrientView> mineralNutrients
) {}