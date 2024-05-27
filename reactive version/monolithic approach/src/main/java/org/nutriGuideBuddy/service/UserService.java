package org.nutriGuideBuddy.service;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.user.*;
import org.nutriGuideBuddy.domain.entity.UserDetails;
import org.nutriGuideBuddy.domain.entity.UserEntity;
import org.nutriGuideBuddy.repository.UserDetailsRepository;
import org.nutriGuideBuddy.repository.UserRepository;
import org.nutriGuideBuddy.utils.JWTUtil;
import org.nutriGuideBuddy.utils.JWTUtilEmailValidation;
import org.nutriGuideBuddy.utils.user.UserHelperFinder;
import org.nutriGuideBuddy.utils.user.UserModifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final UserDetailsRepository userDetailsRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTUtilEmailValidation jwtUtil;
  private final JWTUtil jwtUtil2;
  private final UserHelperFinder userHelper;

  public Mono<UserView> getById() {
    return userHelper.getUser()
        .map(UserView::toView);
  }

  public Mono<JwtResponse> modifyUsername(UserDto userDto) {
    return userHelper.getUser()
        .flatMap(user -> UserModifier.modifyAndSaveUsername(user, userDto))
        .flatMap(user ->
            repository.updateUsernameAndPassword(user.getId(), user)
                .then(Mono.just(user))
                .zipWith(userDetailsRepository.findUserDetailsByUserId(user.getId())
                    .switchIfEmpty(Mono.error(new BadRequestException("User details not found")))
                )
                .map(tuple -> toJwtResponse(tuple.getT1(), tuple.getT2())));
  }

  public Mono<Void> deleteUserById() {
    return userHelper.getUser()
        .flatMap(user -> repository.deleteUserById(user.getId()));
  }

  public Mono<JwtResponse> createUser(UserCreate userDto) {
    if (userDto.email() == null || userDto.email().isBlank()) {
      return Mono.error(new BadRequestException("Invalid email"));
    }

    return repository.findUserByEmail(userDto.email())
        .flatMap(user -> Mono.error(new BadRequestException("User with email " + userDto.email() + " already exists")))
        .switchIfEmpty(createUserAndSave(userDto))
        .cast(JwtResponse.class);
  }

  private Mono<JwtResponse> createUserAndSave(UserCreate userDto) {

    try {
      jwtUtil.validateToken(userDto.token());
    } catch (Exception e) {
      return Mono.error(new BadRequestException("Expired or invalid creation token"));
    }

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
              .zipWith(Mono.fromCallable(() -> userData));
        })
        .map(tuple -> toJwtResponse(tuple.getT2(), tuple.getT1()));
  }

  public Mono<Void> modifyPassword(ResetPasswordDto dto) {
    String token = dto.token();
    String email;
    try {
      email = jwtUtil.getEmailAndValidate(token);
    } catch (Exception e) {
      return Mono.error(new BadRequestException("Invalid password reset token"));
    }

    return repository.findUserByEmail(email)
        .switchIfEmpty(Mono.error(new BadRequestException("User not found")))
        .flatMap(user -> {
          user.setPassword(passwordEncoder.encode(dto.newPassword()));
          return repository.updateUsernameAndPassword(user.getId(), user);
        })
        .then();
  }

  private JwtResponse toJwtResponse(UserEntity user, UserDetails userDetails) {
    return new JwtResponse(
        new UserWithDetailsView(UserView.toView(user), UserDetailsView.toView(userDetails))
        , jwtUtil2.generateToken(userDetails)
    );
  }
}
