package org.trackerwebapp.trackerwebapp.domain.dto.record;

import java.math.BigDecimal;

public record DistributedMacros(
    BigDecimal protein,
    BigDecimal fat,
    BigDecimal carbs,
    BigDecimal omega6,
    BigDecimal omega3
) {

}
