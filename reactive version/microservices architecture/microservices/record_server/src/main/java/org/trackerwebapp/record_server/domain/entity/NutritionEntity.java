package org.trackerwebapp.record_server.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "nutritions")
@Data
public class NutritionEntity {

  @Column("id")
  private String id;
  @Column("name")
  private String name;
  @Column("unit")
  private String unit;
  @Column("amount")
  private BigDecimal amount;
  @Column("food_id")
  private String foodId;
  @Column("user_id")
  private String userId;

  public NutritionEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
