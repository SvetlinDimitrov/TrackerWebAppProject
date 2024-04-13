package org.trackerwebapp.record_server.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.trackerwebapp.record_server.domain.entity.RecordEntity;

public record RecordView(
    String id,
    String name,
    BigDecimal dailyCalories,
    String userId,
    LocalDateTime date) {

  public static RecordView toView(RecordEntity entity) {
    return new RecordView(entity.getId(), entity.getName(), entity.getDailyCalories(),
        entity.getUserId(), entity.getDate());
  }
}
