package org.nutriGuideBuddy.service;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.nutriGuideBuddy.domain.dto.user.UserCreate;
import org.nutriGuideBuddy.domain.dto.user.UserDto;
import org.nutriGuideBuddy.domain.dto.user.UserView;
import org.nutriGuideBuddy.domain.entity.UserDetails;
import org.nutriGuideBuddy.domain.entity.UserEntity;
import org.nutriGuideBuddy.repository.UserDetailsRepository;
import org.nutriGuideBuddy.repository.UserRepository;
import org.nutriGuideBuddy.utils.user.UserModifier;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final UserDetailsRepository userDetailsRepository;
  private final PasswordEncoder passwordEncoder;

  public Mono<UserView> getById(String id) {
    return repository.findUserById(id)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
        .map(UserView::toView);
  }

  public Mono<UserView> modifyUsername(String id, UserDto userDto) {
    return repository.findUserById(id)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
        .flatMap(user -> UserModifier.modifyAndSaveUsername(user, userDto))
        .flatMap(user ->
            repository.updateUsernameAndPassword(user.getId(), user)
                .then(Mono.just(user))
        )
        .map(UserView::toView);
  }

  public Mono<Void> deleteUserById(String id) {
    return repository.findUserById(id)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
        .flatMap(user -> repository.deleteUserById(user.getId()));
  }

  public Mono<UserView> createUser(UserCreate userDto) {
    if (userDto.email() == null || userDto.email().isBlank()) {
      return Mono.error(new BadRequestException("Invalid email"));
    }

    return repository.findUserByEmail(userDto.email())
        .flatMap(user -> Mono.error(new BadRequestException("User with email " + userDto.email() + " already exists")))
        .switchIfEmpty(createUserAndSave(userDto))
        .cast(UserView.class);
  }

  private Mono<UserView> createUserAndSave(UserCreate userDto) {
    return UserModifier.validateAndModifyUserCreation(new UserEntity(), userDto)
        .map(user -> {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          return user;
        })
        .flatMap(repository::save)
        .flatMap(userData -> {
          UserDetails details = new UserDetails();
          details.setUserId(userData.getId());
          return userDetailsRepository.save(details)
              .thenReturn(userData);
        })
        .map(UserView::toView);
  }
}
