package org.trackerwebapp.meal_server.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "inserted_foods")
@Data
public class FoodEntity {

  @Column("id")
  private String id;
  @Column("name")
  private String name;
  @Column("measurement")
  private String measurement;
  @Column("size")
  private BigDecimal size;
  @Column("meal_id")
  private String mealId;
  @Column("user_id")
  private String userId;

  public FoodEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
