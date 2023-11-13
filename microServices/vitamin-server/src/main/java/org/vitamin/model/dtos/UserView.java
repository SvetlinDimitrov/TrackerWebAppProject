package org.vitamin.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
