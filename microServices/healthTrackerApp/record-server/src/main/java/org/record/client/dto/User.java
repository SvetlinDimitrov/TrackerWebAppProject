package org.record.client.dto;

import java.math.BigDecimal;

import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long id;
    private String username;
    private String email;
    private BigDecimal kilograms;
    private BigDecimal height;
    private Integer age;
    private WorkoutState workoutState;
    private Gender gender;

}
