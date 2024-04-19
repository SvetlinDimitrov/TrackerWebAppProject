package org.trackerwebapp.record_server.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.trackerwebapp.record_server.domain.entity.UserDetailsEntity;
import reactor.core.publisher.Mono;

public class UserDetailsValidator {

  public static Mono<UserDetailsEntity> validateUserDetails(UserDetailsEntity entity) {
    return Mono.just(entity)
        .flatMap(data -> {
          if (data.getAge() == null ||
              data.getHeight() == null ||
              data.getGender() == null ||
              data.getWorkoutState() == null ||
              data.getKilograms() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
          } else {
            return Mono.just(entity);
          }
        });
  }
}
