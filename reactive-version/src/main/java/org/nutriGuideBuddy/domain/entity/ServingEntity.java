package org.nutriGuideBuddy.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "servings")
@Data
public class ServingEntity {

  @Id
  private String id;
  @Column("amount")
  private BigDecimal amount;
  @Column("serving_weight")
  private BigDecimal servingWeight;
  @Column("metric")
  private String metric;
  @Column("main")
  private Boolean main;
  @Column("food_id")
  private String foodId;

  public ServingEntity() {
    this.main = Boolean.FALSE;
    this.id = UUID.randomUUID().toString();
  }
}
