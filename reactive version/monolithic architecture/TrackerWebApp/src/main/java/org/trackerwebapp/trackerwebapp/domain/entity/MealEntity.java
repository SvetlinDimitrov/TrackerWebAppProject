package org.trackerwebapp.trackerwebapp.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "meals")
@Data
public class MealEntity {

  @Id
  private String id;
  @Column("name")
  private String name;
  @Column("user_id")
  private String userId;

  public MealEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
