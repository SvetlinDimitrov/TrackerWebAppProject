package org.record.model.dtos;

import lombok.Builder;
import lombok.Data;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;

import java.math.BigDecimal;

@Data
@Builder
public class RecordCreateDto {

    public Gender gender;
    public WorkoutState workoutState;
    private BigDecimal kilograms;
    private BigDecimal height;
    private Integer age;

}
