package org.nutriGuideBuddy.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "users")
@Data
public class UserEntity {

  @Id
  private String id;
  @Column("username")
  private String username;
  @Column("email")
  private String email;
  @Column("password")
  private String password;

  public UserEntity() {
    this.id = UUID.randomUUID().toString();
  }
}
