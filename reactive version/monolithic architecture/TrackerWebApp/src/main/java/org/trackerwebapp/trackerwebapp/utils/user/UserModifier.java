package org.trackerwebapp.trackerwebapp.utils.user;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserCreate;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserDto;
import org.trackerwebapp.trackerwebapp.domain.entity.UserEntity;
import reactor.core.publisher.Mono;

public class UserModifier {

  private static final String regexEmail = "^\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b$";

  public static Mono<UserEntity> modifyAndSaveUsername(UserEntity user, UserDto dto) {
    return Mono.just(user)
        .filter(u -> dto.username() != null && !dto.username().isBlank())
        .flatMap(u -> {
          if (dto.username().trim().length() >= 2) {
            u.setUsername(dto.username());
            return Mono.just(u);
          } else {
            return Mono.error(new BadRequestException("Invalid username length"));
          }
        })
        .switchIfEmpty(Mono.just(user));
  }

  public static Mono<UserEntity> validateAndModifyUserCreation(UserEntity user, UserCreate dto) {
    if(dto.username() != null && !dto.username().isBlank() && dto.username().trim().length() >= 2){
      user.setUsername(dto.username());
    }else{
      return Mono.error(new BadRequestException("Invalid username"));
    }

    if(dto.password() != null && !dto.password().isBlank() && dto.password().trim().length() >= 4){
      user.setPassword(dto.password());
    }else{
      return Mono.error(new BadRequestException("Invalid password"));
    }

    if(dto.email() != null && !dto.email().isBlank() && dto.email().matches(regexEmail)){
      user.setEmail(dto.email());
    }else{
      return Mono.error(new BadRequestException("Invalid email"));
    }

    return Mono.just(user);
  }
}
