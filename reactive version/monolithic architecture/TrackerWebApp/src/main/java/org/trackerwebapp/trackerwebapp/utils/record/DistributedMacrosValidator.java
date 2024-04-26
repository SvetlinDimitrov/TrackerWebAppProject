package org.trackerwebapp.trackerwebapp.utils.record;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.record.DistributedMacros;
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
    if ((macros.fat() == null || macros.fat().compareTo(BigDecimal.ONE) >= 0) ||
        (macros.carbs() == null || macros.carbs().compareTo(BigDecimal.ONE) >= 0) ||
        (macros.protein() == null || macros.protein().compareTo(BigDecimal.ONE) >= 0) ||
        (macros.omega3() == null || macros.omega3().compareTo(BigDecimal.ONE) >= 0) ||
        (macros.omega6() == null || macros.omega6().compareTo(BigDecimal.ONE) >= 0)) {
      return Mono.error(new BadRequestException(
          "if one distributed macro is fill all must be filled with positive numbers less then 1"));
    }
    return Mono.just(macros);
  }
}
