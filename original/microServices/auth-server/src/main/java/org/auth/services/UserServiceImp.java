package org.auth.services;

import org.auth.UserRepository;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.LoginUserDto;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.auth.util.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp extends AbstractUserService {

    public UserServiceImp(UserRepository userRepository, UserValidator validator, PasswordEncoder passwordEncoder) {
        super(validator, passwordEncoder, userRepository);
    }

    public UserView login(LoginUserDto userDto) throws WrongUserCredentialsException {
        if (validator.validateTheLoginCredentials(userDto)) {
            User user = userRepository
                    .findByEmail(userDto.getEmail())
                    .orElseThrow(WrongUserCredentialsException::new);
            if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                return new UserView(user);
            }
        }
        throw new WrongUserCredentialsException();
    }

    public UserView getUserViewById(String userId) {
        return userRepository.findById(userId)
                .map(UserView::new)
                .orElseThrow();
    }

}
