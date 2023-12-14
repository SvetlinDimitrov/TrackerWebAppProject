package org.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
