package org.example.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class User {

    private Long id;
    private String username;
    private String email;
    private BigDecimal kilograms;
    private BigDecimal height;
    private Integer age;
    private WorkoutState workoutState;
    private Gender gender;

    public User (Long id){
        this.id = id;
    }
}
