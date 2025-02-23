package org.auth.features.user.services;

import static org.auth.infrastructure.exceptions.ExceptionMessages.FORBIDDEN;
import static org.auth.infrastructure.exceptions.ExceptionMessages.USER_NOT_FOUND;
import static org.auth.infrastructure.exceptions.ExceptionMessages.USER_NOT_FOUND_EMAIL;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.auth.features.user.entity.User;
import org.auth.features.user.repository.UserRepository;
import org.auth.infrastructure.mappers.UserMapper;
import org.auth.infrastructure.config.security.services.UserDetailsServiceImpl;
import org.example.domain.user.dto.UserEditRequest;
import org.example.domain.user.dto.UserView;
import org.example.domain.user.enums.UserRole;
import org.example.exceptions.throwable.ForbiddenException;
import org.example.exceptions.throwable.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

  private final UserRepository repository;
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserDetailsServiceImpl userDetailsService;

  public UserView getById(UUID userId) {
    checkIfUserIsOwner(userId);

    return repository.findById(userId)
        .map(userMapper::toView)
        .orElseThrow();
  }

  public UserView edit(UserEditRequest userDto, UUID userId) {
    checkIfUserIsOwner(userId);

    var user = findById(userId);

    userMapper.update(user, userDto);

    return userMapper.toView(repository.save(user));
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EMAIL, email));
  }

  public UserView me() {
    return userMapper.toView(userDetailsService.extractUserPrincipal().user());
  }

  private User findById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, userId));
  }

  private void checkIfUserIsOwner(UUID userId) {
    var userDetails = userDetailsService.extractUserPrincipal();

    if (!userDetails.getId().equals(userId) && UserRole.USER == userDetails.user().getRole()) {
      throw new ForbiddenException(FORBIDDEN);
    }
  }
}
