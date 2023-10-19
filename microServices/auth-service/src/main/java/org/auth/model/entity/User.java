package org.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.auth.model.enums.Gender;
import org.auth.model.enums.UserDetails;
import org.auth.model.enums.WorkoutState;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private BigDecimal kilograms;
    private BigDecimal height;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private WorkoutState workoutState;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private UserDetails userDetails = UserDetails.NOT_COMPLETED;

}
