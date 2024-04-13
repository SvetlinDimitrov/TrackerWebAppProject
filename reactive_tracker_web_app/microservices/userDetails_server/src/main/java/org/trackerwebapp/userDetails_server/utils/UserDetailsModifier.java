package org.trackerwebapp.userDetails_server.utils;

import java.math.BigDecimal;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import org.trackerwebapp.userDetails_server.domain.dtos.UserDetailsDto;
import org.trackerwebapp.userDetails_server.domain.entity.UserDetails;
import reactor.core.publisher.Mono;

public class UserDetailsModifier {

  public static Mono<UserDetails> ageModify(UserDetails entity,
      UserDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.age() != null)
        .flatMap(e -> {
          if (dto.age().compareTo(0) > 0) {
            e.setAge(dto.age());
            return Mono.just(e);
          } else {
            return Mono.error(new BadRequestException("Age cannot be 0 or less"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }

  public static Mono<UserDetails> kilogramsModify(UserDetails entity,
      UserDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.kilograms() != null)
        .flatMap(e -> {
          if (dto.kilograms().compareTo(BigDecimal.ZERO) > 0) {
            e.setKilograms(dto.kilograms());
            return Mono.just(e);
          } else {
            return Mono.error(new BadRequestException("Kilograms cannot be 0 or less"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }

  public static Mono<UserDetails> heightModify(UserDetails entity,
      UserDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.height() != null)
        .flatMap(e -> {
          if (dto.height().compareTo(BigDecimal.ZERO) > 0) {
            e.setHeight(dto.height());
            return Mono.just(e);
          } else {
            return Mono.error(new BadRequestException("Height cannot be 0 or less"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }

  public static Mono<UserDetails> workoutStateModify(UserDetails entity,
      UserDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.workoutState() != null)
        .flatMap(e -> {
          e.setWorkoutState(dto.workoutState());
          return Mono.just(e);
        })
        .switchIfEmpty(Mono.just(entity));
  }

  public static Mono<UserDetails> genderModify(UserDetails entity,
      UserDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.gender() != null)
        .flatMap(e -> {
          e.setGender(dto.gender());
          return Mono.just(e);
        })
        .switchIfEmpty(Mono.just(entity));
  }
}
