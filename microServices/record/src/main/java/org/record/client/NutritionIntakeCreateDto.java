package org.record.client;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionIntakeCreateDto {

    @NotNull
    private Gender gender;

    @NotNull
    private BigDecimal caloriesPerDay;

    @NotNull
    private WorkoutState workoutState;
}
