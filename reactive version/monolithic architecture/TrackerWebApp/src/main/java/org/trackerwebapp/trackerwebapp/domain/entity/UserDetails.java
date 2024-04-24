package org.trackerwebapp.trackerwebapp.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.trackerwebapp.trackerwebapp.domain.enums.Gender;
import org.trackerwebapp.trackerwebapp.domain.enums.WorkoutState;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "user_details")
@Data
public class UserDetails {

  @Id
  private String id;
  @Column("kilograms")
  private BigDecimal kilograms;
  @Column("height")
  private BigDecimal height;
  @Column("age")
  private Integer age;
  @Column("workout_state")
  private WorkoutState workoutState;
  @Column("gender")
  private Gender gender;
  @Column("user_id")
  private String userId;

  public UserDetails() {
    this.id = UUID.randomUUID().toString();
  }
}
