package org.food.clients.dto;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class User {

    private String id;
    private String username;
    private String email;
    private BigDecimal kilograms;
    private BigDecimal height;
    private Integer age;
    private WorkoutState workoutState;
    private Gender gender;

    public User (String id){
        this.id = id;
    }
}
