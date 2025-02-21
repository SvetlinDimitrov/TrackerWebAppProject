package org.example.domain.food.embedded.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.food.shared.dto.FoodView;
import org.example.domain.food.shared.dto.FoodPortionView;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FinalFoodView extends FoodView {
    private List<FoodPortionView> foodPortions;
    private String foodGroups;
}
