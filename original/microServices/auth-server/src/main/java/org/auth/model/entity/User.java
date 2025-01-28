package org.auth.model.entity;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.auth.model.enums.Gender;
import org.auth.model.enums.UserDetails;
import org.auth.model.enums.WorkoutState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private BigDecimal kilograms;
    private BigDecimal height;
    private Integer age;
    private Boolean firstRecord;
    private WorkoutState workoutState;
    private Gender gender;
    private UserDetails userDetails = UserDetails.NOT_COMPLETED;

    public boolean isHeFullyRegistered() {
        return kilograms != null &&
            height != null &&
            age != null &&
            gender != null &&
            workoutState != null;
    }
}
