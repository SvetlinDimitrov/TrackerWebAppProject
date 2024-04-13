package org.trackerwebapp.user_server.config;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import org.trackerwebapp.user_server.domain.entity.UserEntity;

@Getter
@Setter
public class UserPrincipal extends User {

  private final String id;
  private final String email;

  public UserPrincipal(UserEntity user) {
    super(user.getEmail(), "", List.of());
    this.id = user.getId();
    this.email = user.getEmail();
  }
  public UserPrincipal(String id , String email) {
    super(email, "", List.of());
    this.id = id;
    this.email = email;
  }
}