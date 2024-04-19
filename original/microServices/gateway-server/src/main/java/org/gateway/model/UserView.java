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
public class UserView {

    private String id;
    private String username;
    private String email;
    private String kilograms;
    private String height;
    private String workoutState;
    private String gender;
    private String userDetails;
    private Integer age;

}
