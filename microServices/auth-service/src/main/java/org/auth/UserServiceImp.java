package org.auth;

import lombok.RequiredArgsConstructor;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.LoginUserDto;
import org.auth.model.dto.RegisterUserDto;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.auth.model.enums.UserDetails;
import org.auth.util.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp {

    private final UserRepository userRepository;
    private final UserValidator validator;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterUserDto userDto) {
        User user = userDto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public UserView login(LoginUserDto userDto) throws WrongUserCredentialsException {
        if (validator.validateTheLoginCredentials.test(userDto)) {
            User user = userRepository
                    .findByEmail(userDto.getEmail())
                    .orElseThrow(WrongUserCredentialsException::new);
            if(passwordEncoder.matches(userDto.getPassword() , user.getPassword())){
                return new UserView(user);
            }
        }
        throw new WrongUserCredentialsException();
    }

    public UserView getUserViewById(Long userId) {
        return userRepository.findById(userId)
                .map(UserView::new)
                .orElseThrow();
    }

    public void editUserEntity(EditUserDto userDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        if (validator.validUsernameChange.test(userDto)) {
            user.setUsername(userDto.getUsername());
        }
        if (validator.validKilogramsChange.test(userDto)) {
            user.setKilograms(userDto.getKilograms());
        }
        if (validator.validWorkoutStateChange.test(userDto)) {
            user.setWorkoutState(userDto.getWorkoutState());
        }
        if (validator.validHeightChange.test(userDto)) {
            user.setHeight(userDto.getHeight());
        }
        if (validator.validGenderChange.test(userDto)) {
            user.setGender(userDto.getGender());
        }
        if (validator.validAgeChange.test(userDto)) {
            user.setAge(userDto.getAge());
        }

        if (validator.validateFullRegistration.test(user)) {
            user.setUserDetails(UserDetails.COMPLETED);
        }

        userRepository.saveAndFlush(user);
    }


}
