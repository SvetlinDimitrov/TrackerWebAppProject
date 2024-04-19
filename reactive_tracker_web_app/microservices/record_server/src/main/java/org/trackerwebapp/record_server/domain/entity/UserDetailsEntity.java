package org.trackerwebapp.record_server.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.trackerwebapp.shared_interfaces.domain.enums.Gender;
import org.trackerwebapp.shared_interfaces.domain.enums.Goals;
import org.trackerwebapp.shared_interfaces.domain.enums.WorkoutState;

@Table(name = "user_details")
@Data
public class UserDetailsEntity {

  @Column("id")
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

  public UserDetailsEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
