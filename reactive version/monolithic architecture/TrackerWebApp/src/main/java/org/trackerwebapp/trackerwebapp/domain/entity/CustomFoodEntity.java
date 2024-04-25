package org.trackerwebapp.trackerwebapp.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "custom_foods")
@Data
public class CustomFoodEntity {

  @Id
  private String id;
  @Column("name")
  private String name;
  @Column("measurement")
  private String measurement;
  @Column("size")
  private BigDecimal size;
  @Column("user_id")
  private String userId;

  public CustomFoodEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
