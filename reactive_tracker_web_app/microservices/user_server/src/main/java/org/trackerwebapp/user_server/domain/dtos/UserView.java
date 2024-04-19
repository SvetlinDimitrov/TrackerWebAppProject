package org.trackerwebapp.user_server.domain.dtos;

import org.trackerwebapp.user_server.domain.entity.UserEntity;

public record UserView(String id, String username, String email) {

  public static UserView toView(UserEntity userEntity) {
    return new UserView(
        userEntity.getId(),
        userEntity.getUsername(),
        userEntity.getEmail()
    );
  }
}
