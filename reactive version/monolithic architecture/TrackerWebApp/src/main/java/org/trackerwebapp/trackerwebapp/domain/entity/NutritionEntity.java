package org.trackerwebapp.trackerwebapp.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "nutritions")
@Data
public class NutritionEntity {

  @Id
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
