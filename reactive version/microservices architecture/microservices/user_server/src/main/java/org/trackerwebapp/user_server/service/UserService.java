package org.trackerwebapp.user_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import org.trackerwebapp.user_server.repository.UserRepository;
import org.trackerwebapp.user_server.domain.dtos.JwtResponse;
import org.trackerwebapp.user_server.domain.dtos.UserDto;
import org.trackerwebapp.user_server.domain.dtos.UserView;
import org.trackerwebapp.user_server.utils.UserModifier;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final JwtService jwtService;

  public Mono<JwtResponse> login(String email) {
    return repository
        .findByEmail(email)
        .switchIfEmpty(Mono.error(new BadRequestException("no user found with the given email")))
        .map(UserView::toView)
        .flatMap(jwtService::generateJwtToken);
  }

  public Mono<UserView> getById(String id) {
    return repository.findById(id)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
        .map(UserView::toView);
  }

  public Mono<UserView> modifyUsername(String id, UserDto userDto) {
    return repository.findById(id)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
        .flatMap(user -> UserModifier.modifyAndSaveUsername(user, userDto))
        .flatMap(user ->
            repository.updateAndFetch(user.getId(), user.getUsername())
                .then(Mono.just(user))
        )
        .map(UserView::toView);
  }

  public Mono<Void> deleteUserById(String id) {
    return repository
        .findById(id)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
        .flatMap(user -> repository.deleteById(user.getId()));
  }
}
