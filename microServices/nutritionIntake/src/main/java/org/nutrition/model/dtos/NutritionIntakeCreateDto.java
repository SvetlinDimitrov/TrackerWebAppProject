package org.nutrition.model.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.nutrition.model.enums.Gender;
import org.nutrition.model.enums.WorkoutState;

import java.math.BigDecimal;

@Data
public class NutritionIntakeCreateDto {

    @NotNull
    private Gender gender;

    @NotNull
    private BigDecimal caloriesPerDay;

    @NotNull
    private WorkoutState workoutState;
}
