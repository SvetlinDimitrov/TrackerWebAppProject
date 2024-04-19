package org.trackerwebapp.customFood_server.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "custom_foods_nutritions")
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

  public NutritionEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
