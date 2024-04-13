package org.trackerwebapp.user_server.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.trackerwebapp.user_server.domain.enums.Gender;
import org.trackerwebapp.user_server.domain.enums.WorkoutState;

@Table(name = "physical_profiles")
@Data
public class PhysicalProfileDetails {

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

  public PhysicalProfileDetails() {
    this.id = UUID.randomUUID().toString();
  }
}
