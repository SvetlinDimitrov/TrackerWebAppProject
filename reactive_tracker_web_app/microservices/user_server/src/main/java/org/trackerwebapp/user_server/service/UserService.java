package org.trackerwebapp.user_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.trackerwebapp.user_server.domain.dtos.JwtResponse;
import org.trackerwebapp.user_server.domain.dtos.PhysicalProfileDetailsDto;
import org.trackerwebapp.user_server.domain.dtos.PhysicalProfileDetailsView;
import org.trackerwebapp.user_server.domain.dtos.UserDto;
import org.trackerwebapp.user_server.domain.dtos.UserView;
import org.trackerwebapp.user_server.domain.entity.UserEntity;
import org.trackerwebapp.user_server.exeption.UserException;
import org.trackerwebapp.user_server.repository.PhysicalProfileRepository;
import org.trackerwebapp.user_server.repository.UserRepository;
import org.trackerwebapp.user_server.utils.PhysicalProfileDetailsModifier;
import org.trackerwebapp.user_server.utils.UserModifier;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final PhysicalProfileRepository profileRepository;
  private final JwtService jwtService;

  public Mono<JwtResponse> login(String email) {
    return repository
        .findByEmail(email)
        .switchIfEmpty(Mono.error(new UserException("no user found with the given email")))
        .flatMap(this::toUserView)
        .flatMap(jwtService::generateJwtToken);
  }

  public Mono<UserView> getUserById(String id) {
    return repository.findById(id)
        .switchIfEmpty(Mono.error(new UserException("no user found with the given id")))
        .flatMap(this::toUserView);
  }

  public Mono<UserView> modifyUser(String id, UserDto userDto) {
    return
        repository.findById(id)
            .switchIfEmpty(Mono.error(new UserException("no user found with the given id")))
            .flatMap(user -> UserModifier.modifyAndSaveUsername(user, userDto))
            .flatMap(user -> modifyAndSavePhysicalDetails(user, userDto.details()))
            .flatMap(repository::save)
            .flatMap(this::toUserView);
  }

  private Mono<UserView> toUserView(UserEntity entity) {
    return profileRepository.findById(entity.getPhysicalProfileDetailsId())
        .switchIfEmpty(Mono.error(new UserException("no user details found with the given id")))
        .map(
            details -> UserView.fromEntity(entity, PhysicalProfileDetailsView.fromEntity(details))
        );
  }

  private Mono<UserEntity> modifyAndSavePhysicalDetails(UserEntity user,
      PhysicalProfileDetailsDto dto) {
    if (dto == null) {
      return Mono.just(user);
    }
    return profileRepository.findById(user.getPhysicalProfileDetailsId())
        .switchIfEmpty(Mono.error(new UserException("no user details found with the given id")))
        .flatMap(entity -> PhysicalProfileDetailsModifier.ageModify(entity, dto))
        .flatMap(entity -> PhysicalProfileDetailsModifier.genderModify(entity, dto))
        .flatMap(entity -> PhysicalProfileDetailsModifier.heightModify(entity, dto))
        .flatMap(entity -> PhysicalProfileDetailsModifier.kilogramsModify(entity, dto))
        .flatMap(entity -> PhysicalProfileDetailsModifier.workoutStateModify(entity, dto))
        .flatMap(profileRepository::save)
        .map(details -> user);
  }


}
