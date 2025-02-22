package org.food.features.custom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.shared.BaseEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "serving_portions")
public class ServingPortion extends BaseEntity {

  @Column(nullable = false)
  private Double amount;

  @Column(nullable = false , name = "serving_weight")
  private Double servingWeight;

  @Column(nullable = false)
  private String metric;

  @Column(nullable = false , name = "is_main")
  private boolean isMain;
}