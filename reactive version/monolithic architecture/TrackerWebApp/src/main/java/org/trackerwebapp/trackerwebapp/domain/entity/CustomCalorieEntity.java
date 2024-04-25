package org.trackerwebapp.trackerwebapp.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "custom_foods_calories")
@Data
public class CustomCalorieEntity {

  @Id
  private String id;
  @Column("amount")
  private BigDecimal amount;
  @Column("food_id")
  private String foodId;

  public CustomCalorieEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
