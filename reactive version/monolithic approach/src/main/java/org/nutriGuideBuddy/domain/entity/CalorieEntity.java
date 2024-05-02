package org.nutriGuideBuddy.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "calories")
@Data
public class CalorieEntity {

  @Id
  private String id;
  @Column("amount")
  private BigDecimal amount;
  @Column("unit")
  private String unit;
  @Column("meal_id")
  private String mealId;
  @Column("food_id")
  private String foodId;
  @Column("user_id")
  private String userId;


  public CalorieEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
