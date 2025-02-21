package org.auth.features.user.services;

import static org.auth.infrastructure.exceptions.ExceptionMessages.USER_NOT_FOUND;
import static org.auth.infrastructure.exceptions.ExceptionMessages.USER_NOT_FOUND_EMAIL;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.auth.features.user.dto.UserCreateRequest;
import org.auth.features.user.entity.User;
import org.auth.features.user.repository.UserRepository;
import org.auth.infrastructure.mappers.UserMapper;
import org.auth.infrastructure.security.dto.CustomUserDetails;
import org.example.domain.user.dto.UserEditRequest;
import org.example.domain.user.dto.UserView;
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

  public UserView getById(UUID userId) {
    return repository.findById(userId)
        .map(userMapper::toView)
        .orElseThrow();
  }

  public UserView create(UserCreateRequest userDto) {
    var userToSave = userMapper.toEntity(userDto);
    var savedUser = repository.save(userToSave);

    String token = gson.toJson(userMapper.toView(repository.save(savedUser)));
    kafkaTemplate.send("USER_FIRST_CREATION", token);

    return userMapper.toView(savedUser);
  }

  public UserView edit(UserEditRequest userDto, UUID userId) {

    var user = findById(userId);

    userMapper.update(user, userDto);

    return userMapper.toView(repository.save(user));
  }

  public void delete(UUID userId) {
    var user = findById(userId);

    String token = gson.toJson(userMapper.toView(user));

    kafkaTemplate.send("USER_DELETION", token);

    repository.delete(user);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EMAIL, email));
  }

  public UserView me() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    var user = customUserDetails.user();
    return userMapper.toView(user);
  }

  private User findById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, userId));
  }
}
