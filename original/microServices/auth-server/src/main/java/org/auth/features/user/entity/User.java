package org.auth.features.user.entity;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.user.enums.Gender;
import org.example.domain.user.enums.UserDetails;
import org.example.domain.user.enums.UserRole;
import org.example.domain.user.enums.WorkoutState;
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
    private Double kilograms;
    private Double height;
    private Integer age;
    private Boolean firstRecord;
    private WorkoutState workoutState;
    private Gender gender;
    private UserDetails userDetails = UserDetails.NOT_COMPLETED;
    private UserRole role = UserRole.USER;

    public boolean isHeFullyRegistered() {
        return kilograms != null &&
            height != null &&
            age != null &&
            gender != null &&
            workoutState != null;
    }
}
