package org.trackerwebapp.trackerwebapp.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "custom_servings")
@Data
public class CustomServingEntity {
  @Id
  private String id;
  @Column("amount")
  private BigDecimal amount;
  @Column("serving_weight")
  private BigDecimal servingWeight;
  @Column("metric")
  private String metric;
  @Column("food_id")
  private String foodId;

  public CustomServingEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
