package org.auth;

import lombok.RequiredArgsConstructor;
import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.LoginUserDto;
import org.auth.model.dto.RegisterUserDto;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.auth.model.enums.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterUserDto userDto) {

    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email "+ email+ " does not exist"));
    }
    public boolean login(LoginUserDto userDto) {
        Optional<User> entity = userRepository.findByEmail(userDto.getEmail());
        return entity.isPresent() && passwordEncoder.matches(userDto.getPassword(), entity.get().getPassword());
    }

    public UserView getUserViewById(Long userId) {
        return userRepository.findById(userId)
                .map(UserView::new)
                .orElseThrow();
    }

    public void editUserEntity(EditUserDto userDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        if (userDto.getUsername() != null && userDto.getUsername().length() > 4 && !userDto.getUsername().isBlank()) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getKilograms() != null) {
            user.setKilograms(userDto.getKilograms());
        }
        if (userDto.getWorkoutState() != null) {
            user.setWorkoutState(userDto.getWorkoutState());
        }
        if (userDto.getHeight() != null) {
            user.setHeight(userDto.getHeight());
        }
        if (userDto.getGender() != null) {
            user.setGender(userDto.getGender());
        }
        if (userDto.getAge() != null) {
            user.setAge(userDto.getAge());
        }

        fillUserWithCompleteDetails(user);
    }

    private void fillUserWithCompleteDetails(User entity) {
        if (entity.getKilograms() != null &&
                entity.getWorkoutState() != null &&
                entity.getGender() != null &&
                entity.getHeight() != null &&
                entity.getAge() != null) {
            entity.setUserDetails(UserDetails.COMPLETED);
        }
        userRepository.save(entity);
    }
}
