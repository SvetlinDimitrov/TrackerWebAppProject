package org.auth.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.auth.model.enums.Gender;
import org.auth.model.enums.WorkoutState;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {

    private String username;
    private BigDecimal kilograms;
    private WorkoutState workoutState;
    private Gender gender;
    private BigDecimal height;
    private Integer age;

}
