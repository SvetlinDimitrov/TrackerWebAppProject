package org.auth.services;

import org.auth.UserRepository;
import org.auth.exceptions.UserNotFoundException;
import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.RegisterUserDto;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.auth.model.enums.UserDetails;
import org.auth.util.GsonWrapper;
import org.auth.util.UserValidator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceKafkaImp extends AbstractUserService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final GsonWrapper gson;

    public UserServiceKafkaImp(
            UserRepository userRepository,
            UserValidator validator,
            PasswordEncoder passwordEncoder,
            KafkaTemplate<String, String> kafkaTemplate,
            GsonWrapper gson) {
        super(validator, passwordEncoder, userRepository);
        this.kafkaTemplate = kafkaTemplate;
        this.gson = gson;
    }

    public void register(RegisterUserDto userDto) {
        User user = userDto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFirstRecord(false);
        userRepository.save(user);

        if (validator.validateFullRegistration(user)) {

            user.setUserDetails(UserDetails.COMPLETED);
            user.setFirstRecord(true);

            String token = gson.toJson(new UserView(user));

            kafkaTemplate.send("USER_FIRST_CREATION", token);
        }

    }

    public UserView editUserEntity(EditUserDto userDto, String userId) throws UserNotFoundException {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user with id :" + userId + " was not found"));

        if (validator.validUsernameChange(userDto)) {
            user.setUsername(userDto.getUsername());
        }
        if (validator.validKilogramsChange(userDto)) {
            user.setKilograms(userDto.getKilograms());
        }
        if (validator.validWorkoutStateChange(userDto)) {
            user.setWorkoutState(userDto.getWorkoutState());
        }
        if (validator.validHeightChange(userDto)) {
            user.setHeight(userDto.getHeight());
        }
        if (validator.validGenderChange(userDto)) {
            user.setGender(userDto.getGender());
        }
        if (validator.validAgeChange(userDto)) {
            user.setAge(userDto.getAge());
        }

        if (validator.validateFullRegistration(user) && !user.getFirstRecord()) {
            user.setUserDetails(UserDetails.COMPLETED);
            user.setFirstRecord(true);

            String token = gson.toJson(new UserView(user));

            kafkaTemplate.send("USER_FIRST_CREATION", token);
        }

        userRepository.save(user);
        return new UserView(user);
    }

    public void deleteUserById(String userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("user with id :" + userId + " was not found"));

        String token = gson.toJson(new UserView(user));

        kafkaTemplate.send("USER_DELETION", token);

        userRepository.deleteById(userId);
    }

}
