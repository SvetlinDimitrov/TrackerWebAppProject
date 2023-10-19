package org.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.auth.model.entity.User;
import org.auth.model.enums.Gender;
import org.auth.model.enums.UserDetails;
import org.auth.model.enums.WorkoutState;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserView {

    private Long id;
    private String username;
    private String email;
    private BigDecimal kilograms;
    private BigDecimal height;
    private WorkoutState workoutState;
    private Gender gender;
    private UserDetails userDetails;
    private Integer age;

    public UserView(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.email = entity.getEmail();
        this.kilograms = entity.getKilograms();
        this.height = entity.getHeight();
        this.workoutState = entity.getWorkoutState();
        this.gender = entity.getGender();
        this.userDetails = entity.getUserDetails();
        this.age = entity.getAge();
    }
}
