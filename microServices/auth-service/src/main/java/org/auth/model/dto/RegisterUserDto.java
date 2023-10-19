package org.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.auth.model.enums.Gender;
import org.auth.model.enums.WorkoutState;
import org.auth.validations.NotUsedEmailConstraint;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @NotBlank
    @Size(min = 4)
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

}
