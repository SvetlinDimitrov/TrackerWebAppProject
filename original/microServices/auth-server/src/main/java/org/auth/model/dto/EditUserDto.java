package org.auth.model.dto;
import java.math.BigDecimal;

import org.auth.model.enums.Gender;
import org.auth.model.enums.WorkoutState;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditUserDto {

    private String username;
    private BigDecimal kilograms;
    private WorkoutState workoutState;
    private Gender gender;
    private BigDecimal height;
    private Integer age;

}
