package org.trackerwebapp.meal_server.domain.entity;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "meals")
@Data
public class MealEntity {

  @Column("id")
  private String id;
  @Column("name")
  private String name;
  @Column("user_id")
  private String userId;

  public MealEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
