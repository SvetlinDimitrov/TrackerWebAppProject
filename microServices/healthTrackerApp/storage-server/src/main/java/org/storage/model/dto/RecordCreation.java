package org.storage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import org.storage.model.enums.Gender;
import org.storage.model.enums.WorkoutState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordCreation {

    private Long recordId;
    private Gender gender;
    private BigDecimal caloriesPerDay;
    private WorkoutState workoutState;

}
