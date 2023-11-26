package org.auth.model.entity;

import java.math.BigDecimal;

import org.auth.model.enums.Gender;
import org.auth.model.enums.UserDetails;
import org.auth.model.enums.WorkoutState;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
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
    private Boolean firstRecord;
    @Enumerated(EnumType.STRING)
    private WorkoutState workoutState;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private UserDetails userDetails = UserDetails.NOT_COMPLETED;

}
