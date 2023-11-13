package org.auth.util;

import java.math.BigDecimal;

import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.LoginUserDto;
import org.auth.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public boolean validateTheLoginCredentials(LoginUserDto userDto) {
        return userDto.getEmail() != null && userDto.getPassword() != null;
    }

    public boolean validUsernameChange(EditUserDto userDto) {
        return userDto.getUsername() != null
                && userDto.getUsername().length() > 4 && !userDto.getUsername().isBlank();
    }

    public boolean validKilogramsChange(EditUserDto userDto) {
        return userDto.getKilograms() != null
                && userDto.getKilograms().compareTo(new BigDecimal(5)) > 0;
    }

    public boolean validWorkoutStateChange(EditUserDto userDto) {
        return userDto.getWorkoutState() != null;
    }

    public boolean validHeightChange(EditUserDto userDto) {
        return userDto.getHeight() != null;
    }

    public boolean validAgeChange(EditUserDto userDto) {
        return userDto.getAge() != null && userDto.getAge() > 0;
    }

    public boolean validGenderChange(EditUserDto userDto) {
        return userDto.getGender() != null;
    }

    public boolean validateFullRegistration(User user) {
        return user.getKilograms() != null &&
                user.getWorkoutState() != null &&
                user.getGender() != null &&
                user.getHeight() != null &&
                user.getAge() != null;
    }
}
