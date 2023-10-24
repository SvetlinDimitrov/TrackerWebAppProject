package org.nutrition.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutrition.model.enums.Gender;
import org.nutrition.model.enums.WorkoutState;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionIntakeCreateDto {

    private Long recordId;
    private Gender gender;
    private BigDecimal caloriesPerDay;
    private WorkoutState workoutState;
}
