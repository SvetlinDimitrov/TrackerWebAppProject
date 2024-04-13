package org.trackerwebapp.user_server.utils;

import org.trackerwebapp.user_server.domain.dtos.UserDto;
import org.trackerwebapp.user_server.domain.entity.UserEntity;
import org.trackerwebapp.user_server.exeption.UserException;
import reactor.core.publisher.Mono;

public class UserModifier {
  public static Mono<UserEntity> modifyAndSaveUsername(UserEntity user, UserDto dto) {
    return Mono.just(user)
        .filter(u -> dto.username() != null && !dto.username().isBlank())
        .flatMap(u -> {
          if (dto.username().trim().length() >= 2) {
            u.setUsername(dto.username());
            return Mono.just(u);
          } else {
            return Mono.error(new UserException("Invalid username length"));
          }
        })
        .switchIfEmpty(Mono.just(user));
  }
}
