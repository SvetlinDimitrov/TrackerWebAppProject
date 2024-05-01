package org.nutriGuideBuddy.utils.user;

import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.user.UserDto;
import org.nutriGuideBuddy.domain.entity.UserEntity;
import org.nutriGuideBuddy.domain.dto.user.UserCreate;
import org.nutriGuideBuddy.domain.enums.ExceptionMessages;
import org.nutriGuideBuddy.utils.Validator;
import reactor.core.publisher.Mono;

public class UserModifier {

  private static final String regexEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

  public static Mono<UserEntity> modifyAndSaveUsername(UserEntity user, UserDto dto) {
    return Mono.just(user)
        .filter(u -> dto.username() != null && !dto.username().isBlank() )
        .flatMap(u -> {
          if (Validator.validateString(dto.username(), 1, 255)) {
            u.setUsername(dto.username());
            return Mono.just(u);
          } else {
            return Mono.error(new BadRequestException(ExceptionMessages.INVALID_STRING_LENGTH_MESSAGE.getMessage() + "for username."));
          }
        })
        .switchIfEmpty(Mono.just(user));
  }

  public static Mono<UserEntity> validateAndModifyUserCreation(UserEntity user, UserCreate dto) {

    if(Validator.validateString(dto.username(), 1, 255)){
      user.setUsername(dto.username());
    }else{
      return Mono.error(new BadRequestException(ExceptionMessages.INVALID_STRING_LENGTH_MESSAGE.getMessage() + "for username."));
    }

    if(Validator.validateString(dto.password(), 4, 255)){
      user.setPassword(dto.password());
    }else{
      return Mono.error(new BadRequestException("Invalid password. Must be between 4 and 255 characters."));
    }

    if(dto.email() != null && !dto.email().isBlank() && dto.email().matches(regexEmail)){
      user.setEmail(dto.email());
    }else{
      return Mono.error(new BadRequestException("Invalid email"));
    }

    return Mono.just(user);
  }
}
