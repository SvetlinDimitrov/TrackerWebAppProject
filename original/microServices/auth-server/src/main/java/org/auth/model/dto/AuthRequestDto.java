package org.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(

    @Email
    @NotBlank
    String email,

    @NotBlank
    String password
) {

}
