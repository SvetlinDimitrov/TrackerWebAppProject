package org.record.features.record.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
import org.record.features.meal.entity.Meal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"meals"})
@ToString(callSuper = true, exclude = {"meals"})
@Entity
@Table(name = "records")
public class Record extends BaseEntity {

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false, unique = true)
  private LocalDateTime date = LocalDateTime.now();

  @Column(nullable = false , name = "daily_calories")
  private Double dailyCalories;

  @OneToMany(
      mappedBy = "record",
      cascade = CascadeType.REMOVE
  )
  private List<Meal> meals = new ArrayList<>();
}