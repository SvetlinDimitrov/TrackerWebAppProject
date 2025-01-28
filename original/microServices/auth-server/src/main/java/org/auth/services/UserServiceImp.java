package org.auth.services;

import static org.auth.exceptions.ExceptionMessages.INVALID_USERNAME_OR_PASSWORD;

import lombok.RequiredArgsConstructor;
import org.auth.UserRepository;
import org.auth.config.security.UserPrincipal;
import org.auth.mappers.UserMapper;
import org.auth.model.dto.AuthRequestDto;
import org.auth.model.dto.UserCreateRequest;
import org.auth.model.dto.UserEditRequest;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.auth.model.enums.UserDetails;
import org.auth.util.GsonWrapper;
import org.example.exceptions.throwable.BadRequestException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final GsonWrapper gson;

    public UserView login(AuthRequestDto userDto) {
        return repository
            .findByEmail(userDto.email())
            .filter(u -> passwordEncoder.matches(userDto.password(), u.getPassword()))
            .map(userMapper::toView)
            .orElseThrow(() -> new BadRequestException(INVALID_USERNAME_OR_PASSWORD));
    }

    public UserView getById(String userId) {
        return repository.findById(userId)
            .map(userMapper::toView)
                .orElseThrow();
    }

    public UserView me() {
        return userMapper.toView(getCurrentLoggedInUser());
    }

    public void register(UserCreateRequest userDto) {
        var userToSave = userMapper.toEntity(userDto);
        var savedUser = repository.save(userToSave);

        if (savedUser.isHeFullyRegistered()) {

            savedUser.setUserDetails(UserDetails.COMPLETED);
            savedUser.setFirstRecord(true);

            String token = gson.toJson(userMapper.toView(repository.save(userToSave)));

            kafkaTemplate.send("USER_FIRST_CREATION", token);
        }

    }

    public UserView edit(UserEditRequest userDto) {

        User currentLoggedInUser = getCurrentLoggedInUser();

        userMapper.update(currentLoggedInUser, userDto);

        if (currentLoggedInUser.isHeFullyRegistered() && !currentLoggedInUser.getFirstRecord()) {
            currentLoggedInUser.setUserDetails(UserDetails.COMPLETED);
            currentLoggedInUser.setFirstRecord(true);

            String token = gson.toJson(userMapper.toView(currentLoggedInUser));

            kafkaTemplate.send("USER_FIRST_CREATION", token);
        }

        return userMapper.toView(repository.save(currentLoggedInUser));
    }

    public void delete() {
        User currentLoggedInUser = getCurrentLoggedInUser();

        String token = gson.toJson(userMapper.toView(currentLoggedInUser));

        kafkaTemplate.send("USER_DELETION", token);

        repository.deleteById(currentLoggedInUser.getId());
    }

    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserPrincipal) {
                return repository.findByEmail(((UserPrincipal) principal).getUsername())
                    .orElseThrow();
            }
        }
        throw new RuntimeException("User not found");
    }
}
