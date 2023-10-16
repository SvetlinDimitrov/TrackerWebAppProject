package org.record.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;

import java.math.BigDecimal;

@Data
public class RecordCreateDto {

    @NotNull
    public Gender gender;
    @NotNull
    public WorkoutState workoutState;
    @NotNull
    private BigDecimal kilograms;
    @NotNull
    private BigDecimal height;
    @Positive
    @NotNull
    private Integer age;

}
