package org.food.features.custom.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "custom_foods")
public class CustomFood extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Embedded
  private Calories calories;

  @Embedded
  private FoodInfo foodInfo;

  @Column(nullable = false)
  private String measurement;

  @Column(nullable = false)
  private Double size;

  @Column(columnDefinition = "BINARY(16)", nullable = false)
  private UUID userId;

  @OneToMany(
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      fetch = FetchType.EAGER
  )
  @JoinTable(
      name = "serving_food_portions",
      joinColumns = @JoinColumn(name = "custom_food_id"),
      inverseJoinColumns = @JoinColumn(name = "portion_id")
  )
  private List<ServingPortion> servingPortions = new ArrayList<>();

  @OneToMany(
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      fetch = FetchType.EAGER
  )
  @JoinTable(
      name = "custom_food_nutrients",
      joinColumns = @JoinColumn(name = "custom_food_id"),
      inverseJoinColumns = @JoinColumn(name = "nutrient_id")
  )
  private List<Nutrient> nutrients = new ArrayList<>();
}