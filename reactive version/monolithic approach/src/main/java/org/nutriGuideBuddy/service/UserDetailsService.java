package org.nutriGuideBuddy.service;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.user.*;
import org.nutriGuideBuddy.repository.UserDetailsRepository;
import org.nutriGuideBuddy.utils.JWTUtil;
import org.nutriGuideBuddy.utils.user.UserDetailsModifier;
import org.nutriGuideBuddy.utils.user.UserHelperFinder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserDetailsService {

  private final UserDetailsRepository repository;
  private final UserHelperFinder userHelper;
  private final JWTUtil jwtUtil;

  public Mono<JwtResponse> modifyUserDetails(UserDetailsDto updateDto) {

    if (updateDto == null) {
      return Mono.error(new BadRequestException("Body cannot be empty"));
    }

    return userHelper.getUser()
        .flatMap(user ->
            repository.findUserDetailsByUserId(user.getId())
                .switchIfEmpty(Mono.error(new BadRequestException("No userDetails found")))
                .flatMap(entity -> UserDetailsModifier.ageModify(entity, updateDto))
                .flatMap(entity -> UserDetailsModifier.genderModify(entity, updateDto))
                .flatMap(entity -> UserDetailsModifier.heightModify(entity, updateDto))
                .flatMap(entity -> UserDetailsModifier.kilogramsModify(entity, updateDto))
                .flatMap(entity -> UserDetailsModifier.workoutStateModify(entity, updateDto))
                .flatMap(entity -> repository.updateUserDetails(entity.getId(), entity))
                .zipWith(Mono.just(user))
        )
        .map(tuple -> new JwtResponse(
            new UserWithDetailsView(UserView.toView(tuple.getT2()), UserDetailsView.toView(tuple.getT1()))
            , jwtUtil.generateToken(tuple.getT1()))
        );
  }

  public Mono<UserDetailsView> getByUserId() throws BadRequestException {

    return userHelper.getUserId()
        .flatMap(repository::findUserDetailsByUserId)
        .switchIfEmpty(Mono.error(new BadRequestException("No userDetails found")))
        .map(UserDetailsView::toView);
  }
}
