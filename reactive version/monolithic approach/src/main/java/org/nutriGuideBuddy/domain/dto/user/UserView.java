package org.nutriGuideBuddy.domain.dto.user;

import org.nutriGuideBuddy.domain.entity.UserEntity;

public record UserView(String id, String username , String email) {

  public static UserView toView(UserEntity userEntity) {
    return new UserView(
        userEntity.getId(),
        userEntity.getUsername(),
        userEntity.getEmail()
    );
  }
}
