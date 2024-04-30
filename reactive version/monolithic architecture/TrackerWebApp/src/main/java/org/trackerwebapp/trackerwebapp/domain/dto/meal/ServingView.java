package org.trackerwebapp.trackerwebapp.domain.dto.meal;

import org.trackerwebapp.trackerwebapp.domain.entity.ServingEntity;

import java.math.BigDecimal;

public record ServingView(
    BigDecimal amount,
    BigDecimal servingWeight,
    String metric
) {
  public static ServingView toView(ServingEntity entity) {
    return new ServingView(
        entity.getAmount(),
        entity.getServingWeight(),
        entity.getMetric()
    );
  }
}