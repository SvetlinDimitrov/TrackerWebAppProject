package org.trackerwebapp.user_server.domain.entity;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
@Data
public class UserEntity {

  @Column("id")
  private String id;
  @Column("username")
  private String username;
  @Column("email")
  private String email;

  public UserEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
