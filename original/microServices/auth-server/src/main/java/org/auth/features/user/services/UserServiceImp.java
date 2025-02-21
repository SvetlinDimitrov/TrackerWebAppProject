package org.auth.features.user.services;

import static org.auth.infrastructure.exceptions.ExceptionMessages.USER_NOT_FOUND;
import static org.auth.infrastructure.exceptions.ExceptionMessages.USER_NOT_FOUND_EMAIL;

import lombok.RequiredArgsConstructor;
import org.auth.UserRepository;
import org.auth.features.user.dto.UserCreateRequest;
import org.example.domain.user.dto.UserEditRequest;
import org.auth.features.user.entity.User;
import org.auth.infrastructure.mappers.UserMapper;
import org.auth.infrastructure.security.dto.CustomUserDetails;
import org.example.domain.user.dto.UserView;
import org.example.domain.user.enums.UserDetails;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.GsonWrapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final GsonWrapper gson;
    private final UserRepository userRepository;

    public UserView getById(String userId) {
        return repository.findById(userId)
            .map(userMapper::toView)
                .orElseThrow();
    }

    public UserView create(UserCreateRequest userDto) {
        var userToSave = userMapper.toEntity(userDto);
        var savedUser = repository.save(userToSave);

        if (savedUser.isHeFullyRegistered()) {

            savedUser.setUserDetails(UserDetails.COMPLETED);
            savedUser.setFirstRecord(true);

            String token = gson.toJson(userMapper.toView(repository.save(savedUser)));

            kafkaTemplate.send("USER_FIRST_CREATION", token);
        }

        return userMapper.toView(savedUser);
    }

    public UserView edit(UserEditRequest userDto, String userId) {

        var user = findById(userId);

        userMapper.update(user, userDto);

        if (user.isHeFullyRegistered() && !user.getFirstRecord()) {
            user.setUserDetails(UserDetails.COMPLETED);
            user.setFirstRecord(true);

            String token = gson.toJson(userMapper.toView(user));

            kafkaTemplate.send("USER_FIRST_CREATION", token);
        }

        return userMapper.toView(repository.save(user));
    }

    public void delete(String userId) {
        var user = findById(userId);

        String token = gson.toJson(userMapper.toView(user));

        kafkaTemplate.send("USER_DELETION", token);

        repository.delete(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EMAIL, email));
    }

    @Override
    public UserView me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        var user = customUserDetails.user();
        return userMapper.toView(user);
    }

    private User findById(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, userId));
    }
}
