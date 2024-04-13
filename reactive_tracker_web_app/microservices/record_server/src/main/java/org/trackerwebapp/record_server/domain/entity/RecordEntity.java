package org.trackerwebapp.record_server.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "records")
@Data
public class RecordEntity {

  @Column("id")
  private String id;
  @Column("name")
  private String name;
  @Column("daily_calories")
  private BigDecimal dailyCalories;
  @Column("user_id")
  private String userId;
  @Column("date")
  private LocalDateTime date;

  public RecordEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
