package org.trackerwebapp.meal_server.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "calories")
@Data
public class CalorieEntity {

  @Column("id")
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
