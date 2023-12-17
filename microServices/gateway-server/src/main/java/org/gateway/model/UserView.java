package org.gateway.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserView {

    private Long id;
    private String username;
    private String email;
    private String kilograms;
    private String height;
    private String workoutState;
    private String gender;
    private String userDetails;
    private Integer age;

}
