package org.trackerwebapp.user_server.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.trackerwebapp.shared_interfaces.domain.enums.Goals;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import org.trackerwebapp.user_server.domain.dtos.UserDetailsDto;
import org.trackerwebapp.user_server.domain.dtos.UserDetailsView;
import org.trackerwebapp.user_server.domain.entity.UserDetails;
import org.trackerwebapp.user_server.repository.UserDetailsRepository;
import org.trackerwebapp.user_server.repository.UserRepository;
import org.trackerwebapp.user_server.utils.UserDetailsModifier;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserDetailsService {

  private final UserRepository userRepository;
  private final UserDetailsRepository repository;

  public Mono<UserDetailsView> createUserDetails(String userId, UserDetailsDto createDto) {
    return userRepository.findById(userId)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401))))
        .flatMap(user ->
            repository.findByUserId(userId)
                .flatMap(
                    data -> Mono.error(new BadRequestException(
                        "There is an existing userDetails with id:" + data.getUserId())))
                .switchIfEmpty(Mono.just(user))
        )
        .map(user -> {
          UserDetails userDetails = new UserDetails();
          userDetails.setUserId(userId);
          return userDetails;
        })
        .flatMap(entity -> UserDetailsModifier.ageModify(entity, createDto))
        .flatMap(entity -> UserDetailsModifier.genderModify(entity, createDto))
        .flatMap(entity -> UserDetailsModifier.heightModify(entity, createDto))
        .flatMap(entity -> UserDetailsModifier.kilogramsModify(entity, createDto))
        .flatMap(entity -> UserDetailsModifier.workoutStateModify(entity, createDto))
        .flatMap(repository::save)
        .map(UserDetailsView::toView);
  }

  public Mono<Void> deleteById(String id, String userId) {
    return getByIdAndUserId(id, userId)
        .flatMap(entity -> repository.deleteByIdAndUserId(entity.getId(), entity.getUserId()));
  }

  public Mono<UserDetailsView> modifyUserDetails(
      String id,
      String userId,
      UserDetailsDto updateDto) {
    if (updateDto == null) {
      return Mono.error(new BadRequestException("Body cannot be empty"));
    }
    return getByIdAndUserId(id, userId)
        .flatMap(entity -> UserDetailsModifier.ageModify(entity, updateDto))
        .flatMap(entity -> UserDetailsModifier.genderModify(entity, updateDto))
        .flatMap(entity -> UserDetailsModifier.heightModify(entity, updateDto))
        .flatMap(entity -> UserDetailsModifier.kilogramsModify(entity, updateDto))
        .flatMap(entity -> UserDetailsModifier.workoutStateModify(entity, updateDto))
        .flatMap(entity ->
            repository.updateAndFetch(
                    entity.getId(),
                    entity.getKilograms(),
                    entity.getHeight(),
                    entity.getAge(),
                    Optional.ofNullable(entity.getWorkoutState())
                        .map(Enum::name)
                        .orElse(null),
                    Optional.ofNullable(entity.getGender())
                        .map(Enum::name)
                        .orElse(null),
                    entity.getUserId())
                .then(Mono.just(entity))
        )
        .map(UserDetailsView::toView);
  }

  private Mono<UserDetails> getByIdAndUserId(String id, String userId) {
    return repository.findByIdAndUserId(id, userId)
        .switchIfEmpty(Mono.error(new BadRequestException("No userDetails found with id:" + id)));
  }

  public Mono<UserDetailsView> getByUserId(String userId) {
    return repository.findByUserId(userId)
        .switchIfEmpty(Mono.error(new BadRequestException("No userDetails found")))
        .map(UserDetailsView::toView);
  }
}
