package org.trackerwebapp.user_server.utils;

import java.math.BigDecimal;
import org.trackerwebapp.user_server.domain.dtos.PhysicalProfileDetailsDto;
import org.trackerwebapp.user_server.domain.entity.PhysicalProfileDetails;
import org.trackerwebapp.user_server.exeption.UserException;
import reactor.core.publisher.Mono;

public class PhysicalProfileDetailsModifier {

  public static Mono<PhysicalProfileDetails> ageModify(PhysicalProfileDetails entity,
      PhysicalProfileDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.age() != null)
        .flatMap(e -> {
          if (dto.age().compareTo(0) > 0) {
            e.setAge(dto.age());
            return Mono.just(e);
          } else {
            return Mono.error(new UserException("Age cannot be 0 or less"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }

  public static Mono<PhysicalProfileDetails> kilogramsModify(PhysicalProfileDetails entity,
      PhysicalProfileDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.kilograms() != null)
        .flatMap(e -> {
          if (dto.kilograms().compareTo(BigDecimal.ZERO) > 0) {
            e.setKilograms(dto.kilograms());
            return Mono.just(e);
          } else {
            return Mono.error(new UserException("Kilograms cannot be 0 or less"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }

  public static Mono<PhysicalProfileDetails> heightModify(PhysicalProfileDetails entity,
      PhysicalProfileDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.height() != null)
        .flatMap(e -> {
          if (dto.height().compareTo(BigDecimal.ZERO) > 0) {
            e.setHeight(dto.height());
            return Mono.just(e);
          } else {
            return Mono.error(new UserException("Height cannot be 0 or less"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }

  public static Mono<PhysicalProfileDetails> workoutStateModify(PhysicalProfileDetails entity,
      PhysicalProfileDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.workoutState() != null)
        .flatMap(e -> {
          e.setWorkoutState(dto.workoutState());
          return Mono.just(e);
        })
        .switchIfEmpty(Mono.just(entity));
  }
  public static Mono<PhysicalProfileDetails> genderModify(PhysicalProfileDetails entity,
      PhysicalProfileDetailsDto dto) {
    return Mono.just(entity)
        .filter(e -> dto.gender() != null)
        .flatMap(e -> {
          e.setGender(dto.gender());
          return Mono.just(e);
        })
        .switchIfEmpty(Mono.just(entity));
  }
}
