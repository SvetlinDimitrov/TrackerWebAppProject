package org.auth.services;

import org.auth.UserRepository;
import org.auth.config.security.JwtUtil;
import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.JwtTokenView;
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

@Service
public class UserServiceKafkaImp extends AbstractUserService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JwtUtil jwtUtil;

    public UserServiceKafkaImp(UserRepository userRepository, UserValidator validator, PasswordEncoder passwordEncoder,
            KafkaTemplate<String, String> kafkaTemplate,
            JwtUtil jwtUtil) {
        super(validator, passwordEncoder, userRepository);
        this.kafkaTemplate = kafkaTemplate;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterUserDto userDto) {
        User user = userDto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFirstRecord(false);
        userRepository.saveAndFlush(user);

        if (validator.validateFullRegistration.test(user)) {

            user.setUserDetails(UserDetails.COMPLETED);
            user.setFirstRecord(true);

            JwtTokenView token = jwtUtil.createJwtToken(new UserView(user));
            Message<String> message = MessageBuilder
                    .withPayload(token.getToken())
                    .setHeader(KafkaHeaders.TOPIC, "user-first-creation")
                    .build();

            kafkaTemplate.send(message);
        }

    }

    public UserView editUserEntity(EditUserDto userDto, Long userId) {

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

        if (validator.validateFullRegistration.test(user) && !user.getFirstRecord()) {
            user.setUserDetails(UserDetails.COMPLETED);
            user.setFirstRecord(true);

            JwtTokenView token = jwtUtil.createJwtToken(new UserView(user));
            Message<String> message = MessageBuilder
                    .withPayload(token.getToken())
                    .setHeader(KafkaHeaders.TOPIC, "user-first-creation")
                    .build();

            kafkaTemplate.send(message);
        }

        userRepository.saveAndFlush(user);
        return new UserView(user);
    }

}
