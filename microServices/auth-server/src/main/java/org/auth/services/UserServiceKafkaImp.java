package org.auth.services;

import org.auth.UserRepository;
import org.auth.config.security.JwtUtil;
import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.RegisterUserDto;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.auth.model.enums.UserDetails;
import org.auth.util.UserValidator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class UserServiceKafkaImp extends AbstractUserService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private Gson gson = new Gson();

    public UserServiceKafkaImp(
            UserRepository userRepository,
            UserValidator validator,
            PasswordEncoder passwordEncoder,
            KafkaTemplate<String, String> kafkaTemplate) {
        super(validator, passwordEncoder, userRepository);
        this.kafkaTemplate = kafkaTemplate;

    }

    public void register(RegisterUserDto userDto) {
        User user = userDto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFirstRecord(false);
        userRepository.saveAndFlush(user);

        if (validator.validateFullRegistration(user)) {

            user.setUserDetails(UserDetails.COMPLETED);
            user.setFirstRecord(true);

            String token = gson.toJson(new UserView(user));

            Message<String> message = MessageBuilder
                    .withPayload(token)
                    .setHeader(KafkaHeaders.TOPIC, "user-first-creation")
                    .build();

            kafkaTemplate.send(message);
        }

    }

    public UserView editUserEntity(EditUserDto userDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow();

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

            Message<String> message = MessageBuilder
                    .withPayload(token)
                    .setHeader(KafkaHeaders.TOPIC, "user-first-creation")
                    .build();

            kafkaTemplate.send(message);
        }

        userRepository.saveAndFlush(user);
        return new UserView(user);
    }

}
