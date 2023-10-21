package org.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.auth.model.entity.User;
import org.auth.model.enums.Gender;
import org.auth.model.enums.WorkoutState;
import org.auth.validations.NotUsedEmailConstraint;

import java.math.BigDecimal;

@Data
public class RegisterUserDto {

    @Size(min = 4)
    @NotBlank
    @NotNull
    private String username;

    @NotUsedEmailConstraint
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 5)
    private String password;
    private BigDecimal kg;
    private WorkoutState workoutState;
    private Gender gender;
    private BigDecimal height;
    private Integer age;

    public User toUser (){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setKilograms(kg);
        user.setWorkoutState(workoutState);
        user.setGender(gender);
        user.setHeight(height);
        user.setAge(age);
        return user;
    }

}
