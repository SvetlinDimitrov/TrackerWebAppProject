package org.trackerwebapp.record_server.utils;

import org.trackerwebapp.record_server.domain.dto.DistributedMacros;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class DistributedMacrosValidator {

  public static Mono<DistributedMacros> validate(DistributedMacros macros) {
    if (macros == null) {
      return Mono.just(new DistributedMacros(
          BigDecimal.valueOf(0.25),
          BigDecimal.valueOf(0.25),
          BigDecimal.valueOf(0.50),
          BigDecimal.valueOf(0.07),
          BigDecimal.valueOf(0.10)
      ));
    }
    if (macros.fat() == null ||
        macros.carbs() == null ||
        macros.protein() == null ||
        macros.omega6() == null ||
        macros.omega3() == null ) {
      return Mono.error(new BadRequestException(
          "if one distributed macro is fill all must be filled with positive numbers less then 1"));
    }
    return Mono.just(macros);
  }
}
